package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;
import ro.unibuc.fmi.awbd.controller.models.ProductCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ProductIngredientCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ProductIngredientUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.ProductUpdateDto;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociationId;
import ro.unibuc.fmi.awbd.domain.product.model.Product;
import ro.unibuc.fmi.awbd.service.product.model.ProductFilter;
import ro.unibuc.fmi.awbd.service.product.model.ProductPageElementDetails;

import java.util.List;

public class ProductFixtures {
    private ProductFixtures() {
    }

    public static Page<ProductPageElementDetails> getPageOfProductPageElementDetailsFixture() {
        return Page.<ProductPageElementDetails>builder()
                .items(List.of(getProductPageElementDetailsFixture()))
                .paginationInformation(PaginationInformationFixtures.getPaginationInformationFixture())
                .build();
    }

    public static ProductCreationDto getProductCreationDtoFixture() {
        return new ProductCreationDto("New Name", 1000., "New Description", List.of(getProductIngredientCreationDtoFixture()));
    }

    public static ProductUpdateDto getProductUpdateDtoFixture() {
        return new ProductUpdateDto("New Name", 1000., "New Description", List.of(getProductIngredientUpdateDtoFixture()));
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

    public static PageRequest<ProductFilter> getPageRequestFixture() {
        val productFilter = new ProductFilter("name", "description");
        val paginationRequest = new PaginationRequest(1, 1, "+name");
        return PageRequest.of(productFilter, paginationRequest);
    }

    private static ProductPageElementDetails getProductPageElementDetailsFixture() {
        return ProductPageElementDetails.builder()
                .id(1L)
                .name("name")
                .description("address")
                .price(100.)
                .calories(1000.)
                .build();
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

    private static ProductIngredientCreationDto getProductIngredientCreationDtoFixture() {
        return new ProductIngredientCreationDto(1L, 100.);
    }

    private static ProductIngredientUpdateDto getProductIngredientUpdateDtoFixture() {
        return new ProductIngredientUpdateDto(1L, 100.);
    }
}
