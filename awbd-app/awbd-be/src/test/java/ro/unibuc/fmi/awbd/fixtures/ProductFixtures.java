package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociationId;
import ro.unibuc.fmi.awbd.domain.product.model.Product;

import java.util.List;

public class ProductFixtures {
    private ProductFixtures() {
    }

    public static Product getProductFixture() {
        val product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setPrice(100.);
        product.setDescription("Product Description");
        product.setIngredientProductAssociations(List.of(getIngredientProductAssociationFixture(product)));
        return product;
    }

    private static IngredientProductAssociation getIngredientProductAssociationFixture(Product product) {
        val ingredient = IngredientFixtures.getIngredientFixture();
        return IngredientProductAssociation.builder()
                .quantity(100.)
                .id(IngredientProductAssociationId.builder()
                        .ingredientId(ingredient.getId())
                        .productId(product.getId())
                        .build())
                .ingredient(ingredient)
                .product(product)
                .build();
    }
}
