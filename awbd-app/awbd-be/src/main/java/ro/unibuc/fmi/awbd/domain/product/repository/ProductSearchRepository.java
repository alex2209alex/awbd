package ro.unibuc.fmi.awbd.domain.product.repository;

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
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation_;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient_;
import ro.unibuc.fmi.awbd.domain.product.model.Product;
import ro.unibuc.fmi.awbd.domain.product.model.Product_;
import ro.unibuc.fmi.awbd.service.product.model.ProductFilter;
import ro.unibuc.fmi.awbd.service.product.model.ProductPageElementDetails;
import ro.unibuc.fmi.awbd.service.product.model.ProductSortableColumn;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<ProductPageElementDetails> getProductsPage(PageRequest<ProductFilter> pageRequest) {
        val products = fetchProducts(pageRequest);
        val count = countProducts(pageRequest);
        return Page.<ProductPageElementDetails>builder()
                .items(products)
                .paginationInformation(PaginationInformation.of(count, pageRequest.getPagination()))
                .build();
    }

    private List<ProductPageElementDetails> fetchProducts(PageRequest<ProductFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val query = cb.createQuery(ProductPageElementDetails.class);
        val root = query.from(Product.class);
        Join<Product, IngredientProductAssociation> productIngredientProductAssociationJoin = root.join(Product_.INGREDIENT_PRODUCT_ASSOCIATIONS);
        Join<IngredientProductAssociation, Ingredient> ingredientProductAssociationIngredientJoin = productIngredientProductAssociationJoin.join(IngredientProductAssociation_.INGREDIENT);
        Expression<Double> calories = cb.sum(cb.prod(productIngredientProductAssociationJoin.get(IngredientProductAssociation_.QUANTITY), ingredientProductAssociationIngredientJoin.get(Ingredient_.CALORIES)));

        query.multiselect(
                root.get(Product_.ID),
                root.get(Product_.NAME),
                root.get(Product_.PRICE),
                root.get(Product_.DESCRIPTION),
                calories.alias("calories")
        );

        val sortingColumn = getSortingColumn(pageRequest, root, calories);
        query.orderBy(pageRequest.getPagination().getSort().startsWith("+") ? cb.asc(sortingColumn) : cb.desc(sortingColumn));

        query.where(getSearchPredicates(pageRequest, cb, root));
        query.groupBy(root.get(Product_.ID));

        return entityManager.createQuery(query)
                .setFirstResult((pageRequest.getPagination().getPage() - 1) * pageRequest.getPagination().getPageSize())
                .setMaxResults(pageRequest.getPagination().getPageSize())
                .getResultList();
    }

    private Expression<?> getSortingColumn(PageRequest<ProductFilter> pageRequest, Root<Product> root, Expression<Double> calories) {
        val sortingColumn = ProductSortableColumn.fromValue(pageRequest.getPagination().getSort().substring(1));
        return switch (sortingColumn) {
            case ProductSortableColumn.NAME -> root.get(Product_.NAME);
            case ProductSortableColumn.PRICE -> root.get(Product_.PRICE);
            case ProductSortableColumn.DESCRIPTION -> root.get(Product_.DESCRIPTION);
            case ProductSortableColumn.CALORIES -> calories;
        };
    }

    private Long countProducts(PageRequest<ProductFilter> pageRequest) {
        val cb = entityManager.getCriteriaBuilder();
        val countQuery = cb.createQuery(Long.class);
        val root = countQuery.from(Product.class);

        countQuery.select(cb.count(root));

        countQuery.where(getSearchPredicates(pageRequest, cb, root));

        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    private Predicate[] getSearchPredicates(
            PageRequest<ProductFilter> pageRequest,
            CriteriaBuilder cb,
            Root<Product> root) {
        val predicates = new ArrayList<Predicate>();

        val name = pageRequest.getFilter().getName();
        if (!StringUtils.isNullOrBlank(name)) {
            predicates.add(cb.like(cb.lower(root.get(Product_.NAME)), "%" + name.toLowerCase() + "%"));
        }

        val description = pageRequest.getFilter().getDescription();
        if (!StringUtils.isNullOrBlank(description)) {
            predicates.add(cb.like(cb.lower(root.get(Product_.DESCRIPTION)), "%" + description.toLowerCase() + "%"));
        }

        return predicates.toArray(new Predicate[]{});
    }
}
