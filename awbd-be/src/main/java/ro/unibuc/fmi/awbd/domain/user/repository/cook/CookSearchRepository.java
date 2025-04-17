package ro.unibuc.fmi.awbd.domain.user.repository.cook;

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
import ro.unibuc.fmi.awbd.domain.user.model.cook.Cook;
import ro.unibuc.fmi.awbd.domain.user.model.cook.Cook_;
import ro.unibuc.fmi.awbd.service.cook.model.CookFilter;
import ro.unibuc.fmi.awbd.service.cook.model.CookPageElementDetails;
import ro.unibuc.fmi.awbd.service.cook.model.CookSortableColumn;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CookSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<CookPageElementDetails> getCooksPage(PageRequest<CookFilter> pageRequest) {
        val cooks = fetchCooks(pageRequest);
        val count = countCooks(pageRequest);
        return Page.<CookPageElementDetails>builder()
                .items(cooks)
                .paginationInformation(PaginationInformation.of(count, pageRequest.getPagination()))
                .build();
    }

    private List<CookPageElementDetails> fetchCooks(PageRequest<CookFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val query = cb.createQuery(CookPageElementDetails.class);
        val root = query.from(Cook.class);

        query.multiselect(
                root.get(User_.ID),
                root.get(User_.EMAIL),
                root.get(User_.NAME),
                root.get(Cook_.SALARY)
        );

        val sortingColumn = getSortingColumn(pageRequest, root);
        query.orderBy(pageRequest.getPagination().getSort().startsWith("+") ? cb.asc(sortingColumn) : cb.desc(sortingColumn));

        query.where(getSearchPredicates(pageRequest, cb, root));

        return entityManager.createQuery(query)
                .setFirstResult((pageRequest.getPagination().getPage() - 1) * pageRequest.getPagination().getPageSize())
                .setMaxResults(pageRequest.getPagination().getPageSize())
                .getResultList();
    }

    private Path<?> getSortingColumn(PageRequest<CookFilter> pageRequest, Root<Cook> root) {
        val sortingColumn = CookSortableColumn.fromValue(pageRequest.getPagination().getSort().substring(1));
        return switch (sortingColumn) {
            case CookSortableColumn.EMAIL -> root.get(User_.EMAIL);
            case CookSortableColumn.NAME -> root.get(User_.NAME);
            case CookSortableColumn.SALARY -> root.get(Cook_.SALARY);
        };
    }

    private Long countCooks(PageRequest<CookFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val countQuery = cb.createQuery(Long.class);
        val root = countQuery.from(Cook.class);

        countQuery.select(cb.count(root));

        countQuery.where(getSearchPredicates(pageRequest, cb, root));

        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    private Predicate[] getSearchPredicates(
            PageRequest<CookFilter> pageRequest,
            CriteriaBuilder cb,
            Root<Cook> root) {
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
