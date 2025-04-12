package ro.unibuc.fmi.awbd.domain.ingredient.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationInformation;
import ro.unibuc.fmi.awbd.common.utils.StringUtils;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient_;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer_;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientFilter;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientPageElementDetails;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientSortableColumn;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    public Page<IngredientPageElementDetails> getIngredientsPage(PageRequest<IngredientFilter> pageRequest) {
        val ingredients = fetchIngredients(pageRequest);
        val count = countIngredients(pageRequest);
        return Page.<IngredientPageElementDetails>builder()
                .items(ingredients)
                .paginationInformation(PaginationInformation.of(count, pageRequest.getPagination()))
                .build();
    }

    private List<IngredientPageElementDetails> fetchIngredients(PageRequest<IngredientFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val query = cb.createQuery(IngredientPageElementDetails.class);
        val root = query.from(Ingredient.class);
        Join<Ingredient, Producer> ingredientProducerJoin = root.join(Ingredient_.PRODUCER);

        query.multiselect(
                root.get(Ingredient_.ID),
                root.get(Ingredient_.NAME),
                root.get(Ingredient_.PRICE),
                root.get(Ingredient_.CALORIES),
                ingredientProducerJoin.get(Producer_.NAME)
        );

        val sortingColumn = getSortingColumn(pageRequest, root, ingredientProducerJoin);
        query.orderBy(pageRequest.getPagination().getSort().startsWith("+") ? cb.asc(sortingColumn) : cb.desc(sortingColumn));

        query.where(getSearchPredicates(pageRequest, cb, root, ingredientProducerJoin));

        return entityManager.createQuery(query)
                .setFirstResult((pageRequest.getPagination().getPage() - 1) * pageRequest.getPagination().getPageSize())
                .setMaxResults(pageRequest.getPagination().getPageSize())
                .getResultList();
    }

    private Path<?> getSortingColumn(PageRequest<IngredientFilter> pageRequest, Root<Ingredient> root, Join<Ingredient, Producer> ingredientProducerJoin) {
        val sortingColumn = IngredientSortableColumn.fromValue(pageRequest.getPagination().getSort().substring(1));
        return switch (sortingColumn) {
            case IngredientSortableColumn.NAME -> root.get(Ingredient_.NAME);
            case IngredientSortableColumn.PRICE -> root.get(Ingredient_.PRICE);
            case IngredientSortableColumn.CALORIES -> root.get(Ingredient_.CALORIES);
            case IngredientSortableColumn.PRODUCER -> ingredientProducerJoin.get(Producer_.NAME);
        };
    }

    private Long countIngredients(PageRequest<IngredientFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val countQuery = cb.createQuery(Long.class);
        val root = countQuery.from(Ingredient.class);
        Join<Ingredient, Producer> ingredientProducerJoin = root.join(Ingredient_.PRODUCER);

        countQuery.select(cb.count(root));

        countQuery.where(getSearchPredicates(pageRequest, cb, root, ingredientProducerJoin));

        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    private Predicate[] getSearchPredicates(
            PageRequest<IngredientFilter> pageRequest,
            CriteriaBuilder cb,
            Root<Ingredient> root,
            Join<Ingredient, Producer> ingredientProducerJoin) {
        val predicates = new ArrayList<Predicate>();

        val name = pageRequest.getFilter().getName();
        if (!StringUtils.isNullOrBlank(name)) {
            predicates.add(cb.like(cb.lower(root.get(Ingredient_.NAME)), "%" + name.toLowerCase() + "%"));
        }

        val producer = pageRequest.getFilter().getProducer();
        if (!StringUtils.isNullOrBlank(producer)) {
            predicates.add(cb.like(cb.lower(ingredientProducerJoin.get(Producer_.NAME)), "%" + producer.toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[] {});
    }
}
