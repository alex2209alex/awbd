package ro.unibuc.fmi.awbd.domain.user.repository.courier;

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
import ro.unibuc.fmi.awbd.domain.user.model.User_;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier_;
import ro.unibuc.fmi.awbd.service.courier.model.CourierFilter;
import ro.unibuc.fmi.awbd.service.courier.model.CourierPageElementDetails;
import ro.unibuc.fmi.awbd.service.courier.model.CourierSortableColumn;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourierSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<CourierPageElementDetails> getCouriersPage(PageRequest<CourierFilter> pageRequest) {
        val couriers = fetchCouriers(pageRequest);
        val count = countCouriers(pageRequest);
        return Page.<CourierPageElementDetails>builder()
                .items(couriers)
                .paginationInformation(PaginationInformation.of(count, pageRequest.getPagination()))
                .build();
    }

    private List<CourierPageElementDetails> fetchCouriers(PageRequest<CourierFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val query = cb.createQuery(CourierPageElementDetails.class);
        val root = query.from(Courier.class);

        query.multiselect(
                root.get(User_.ID),
                root.get(User_.EMAIL),
                root.get(User_.NAME),
                root.get(Courier_.PHONE_NUMBER),
                root.get(Courier_.SALARY)
        );

        val sortingColumn = getSortingColumn(pageRequest, root);
        query.orderBy(pageRequest.getPagination().getSort().startsWith("+") ? cb.asc(sortingColumn) : cb.desc(sortingColumn));

        query.where(getSearchPredicates(pageRequest, cb, root));

        return entityManager.createQuery(query)
                .setFirstResult((pageRequest.getPagination().getPage() - 1) * pageRequest.getPagination().getPageSize())
                .setMaxResults(pageRequest.getPagination().getPageSize())
                .getResultList();
    }

    private Path<?> getSortingColumn(PageRequest<CourierFilter> pageRequest, Root<Courier> root) {
        val sortingColumn = CourierSortableColumn.fromValue(pageRequest.getPagination().getSort().substring(1));
        return switch (sortingColumn) {
            case CourierSortableColumn.EMAIL -> root.get(User_.EMAIL);
            case CourierSortableColumn.NAME -> root.get(User_.NAME);
            case CourierSortableColumn.PHONE_NUMBER -> root.get(Courier_.PHONE_NUMBER);
            case CourierSortableColumn.SALARY -> root.get(Courier_.SALARY);
        };
    }

    private Long countCouriers(PageRequest<CourierFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val countQuery = cb.createQuery(Long.class);
        val root = countQuery.from(Courier.class);

        countQuery.select(cb.count(root));

        countQuery.where(getSearchPredicates(pageRequest, cb, root));

        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    private Predicate[] getSearchPredicates(
            PageRequest<CourierFilter> pageRequest,
            CriteriaBuilder cb,
            Root<Courier> root) {
        val predicates = new ArrayList<Predicate>();

        val email = pageRequest.getFilter().getEmail();
        if (!StringUtils.isNullOrBlank(email)) {
            predicates.add(cb.like(cb.lower(root.get(User_.EMAIL)), "%" + email.toLowerCase() + "%"));
        }

        val name = pageRequest.getFilter().getName();
        if (!StringUtils.isNullOrBlank(name)) {
            predicates.add(cb.like(cb.lower(root.get(User_.NAME)), "%" + name.toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[]{});
    }
}
