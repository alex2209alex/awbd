package ro.unibuc.fmi.awbd.domain.producer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationInformation;
import ro.unibuc.fmi.awbd.common.utils.StringUtils;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer_;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerFilter;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerPageElementDetails;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerSortableColumn;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProducerSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<ProducerPageElementDetails> getProducersPage(PageRequest<ProducerFilter> pageRequest) {
        val producers = fetchProducers(pageRequest);
        val count = countProducers(pageRequest);
        return Page.<ProducerPageElementDetails>builder()
                .items(producers)
                .paginationInformation(PaginationInformation.of(count, pageRequest.getPagination()))
                .build();
    }

    private List<ProducerPageElementDetails> fetchProducers(PageRequest<ProducerFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val query = cb.createQuery(ProducerPageElementDetails.class);
        val root = query.from(Producer.class);

        query.multiselect(
                root.get(Producer_.ID),
                root.get(Producer_.NAME),
                root.get(Producer_.ADDRESS)
        );

        val sortingColumn = getSortingColumn(pageRequest, root);
        query.orderBy(pageRequest.getPagination().getSort().startsWith("+") ? cb.asc(sortingColumn) : cb.desc(sortingColumn));

        query.where(getSearchPredicates(pageRequest, cb, root));

        return entityManager.createQuery(query)
                .setFirstResult((pageRequest.getPagination().getPage() - 1) * pageRequest.getPagination().getPageSize())
                .setMaxResults(pageRequest.getPagination().getPageSize())
                .getResultList();
    }

    private Path<?> getSortingColumn(PageRequest<ProducerFilter> pageRequest, Root<Producer> root) {
        val sortingColumn = ProducerSortableColumn.fromValue(pageRequest.getPagination().getSort().substring(1));
        return switch (sortingColumn) {
            case ProducerSortableColumn.NAME -> root.get(Producer_.NAME);
            case ProducerSortableColumn.ADDRESS -> root.get(Producer_.ADDRESS);
        };
    }

    private Long countProducers(PageRequest<ProducerFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val countQuery = cb.createQuery(Long.class);
        val root = countQuery.from(Producer.class);

        countQuery.select(cb.count(root));

        countQuery.where(getSearchPredicates(pageRequest, cb, root));

        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    private Predicate[] getSearchPredicates(
            PageRequest<ProducerFilter> pageRequest,
            CriteriaBuilder cb,
            Root<Producer> root) {
        val predicates = new ArrayList<Predicate>();

        val name = pageRequest.getFilter().getName();
        if (!StringUtils.isNullOrBlank(name)) {
            predicates.add(cb.like(cb.lower(root.get(Producer_.NAME)), "%" + name.toLowerCase() + "%"));
        }

        val address = pageRequest.getFilter().getAddress();
        if (!StringUtils.isNullOrBlank(address)) {
            predicates.add(cb.like(cb.lower(root.get(Producer_.ADDRESS)), "%" + address.toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[] {});
    }
}
