package ro.unibuc.fmi.awbd.service.product.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.fixtures.ProductFixtures;

import java.util.List;

@SpringBootTest(classes = {ProductMapperImpl.class})
class ProductMapperTest {
    @Autowired
    private ProductMapper productMapper;

    @Test
    void testMapToProductFilter() {
        Assertions.assertNull(productMapper.mapToProductFilter(null, null));

        val name = "name";
        val description = "description";

        var productFilter = productMapper.mapToProductFilter(name, description);

        Assertions.assertNotNull(productFilter);
        Assertions.assertEquals(name, productFilter.getName());
        Assertions.assertEquals(description, productFilter.getDescription());

        productFilter = productMapper.mapToProductFilter(null, description);

        Assertions.assertNotNull(productFilter);
        Assertions.assertNull(productFilter.getName());
        Assertions.assertEquals(description, productFilter.getDescription());

        productFilter = productMapper.mapToProductFilter(name, null);

        Assertions.assertNotNull(productFilter);
        Assertions.assertEquals(name, productFilter.getName());
        Assertions.assertNull(productFilter.getDescription());
    }

    @Test
    void testMapToProductsPageDto() {
        Assertions.assertNull(productMapper.mapToProductsPageDto(null));

        val page = ProductFixtures.getPageOfProductPageElementDetailsFixture();

        val productsPageDto = productMapper.mapToProductsPageDto(page);

        Assertions.assertNotNull(productsPageDto);

        Assertions.assertNotNull(productsPageDto.getPagination());
        Assertions.assertEquals(productsPageDto.getPagination().getPage(), page.getPaginationInformation().getPage());
        Assertions.assertEquals(productsPageDto.getPagination().getPageSize(), page.getPaginationInformation().getPageSize());
        Assertions.assertEquals(productsPageDto.getPagination().getPagesTotal(), page.getPaginationInformation().getPagesTotal());
        Assertions.assertEquals(productsPageDto.getPagination().getHasNextPage(), page.getPaginationInformation().isHasNextPage());
        Assertions.assertEquals(productsPageDto.getPagination().getItemsTotal(), page.getPaginationInformation().getItemsTotal());
        Assertions.assertEquals(productsPageDto.getPagination().getSort(), page.getPaginationInformation().getSort());

        Assertions.assertNotNull(productsPageDto.getItems());
        Assertions.assertEquals(page.getItems().size(), productsPageDto.getItems().size());
        for (int i = 0; i < productsPageDto.getItems().size(); i++) {
            Assertions.assertEquals(page.getItems().get(i).getId(), productsPageDto.getItems().get(i).getId());
            Assertions.assertEquals(page.getItems().get(i).getName(), productsPageDto.getItems().get(i).getName());
            Assertions.assertEquals(page.getItems().get(i).getDescription(), productsPageDto.getItems().get(i).getDescription());
            Assertions.assertEquals(page.getItems().get(i).getPrice(), productsPageDto.getItems().get(i).getPrice());
            Assertions.assertEquals(page.getItems().get(i).getCalories(), productsPageDto.getItems().get(i).getCalories());
        }
    }

    @Test
    void testMapToProductSearchDetailsDtos() {
        Assertions.assertNull(productMapper.mapToProductSearchDetailsDtos(null));

        val product = ProductFixtures.getProductFixture();

        val productSearchDetailsDtos = productMapper.mapToProductSearchDetailsDtos(List.of(product));

        Assertions.assertNotNull(productSearchDetailsDtos);
        Assertions.assertEquals(1, productSearchDetailsDtos.size());
        Assertions.assertEquals(product.getId(), productSearchDetailsDtos.getFirst().getId());
        Assertions.assertEquals(product.getName(), productSearchDetailsDtos.getFirst().getName());
    }

    @Test
    void testMapToProductDetailsDto() {
        Assertions.assertNull(productMapper.mapToProductDetailsDto(null));

        val product = ProductFixtures.getProductFixture();

        val productDetailsDto = productMapper.mapToProductDetailsDto(product);

        Assertions.assertNotNull(productDetailsDto);
        Assertions.assertEquals(product.getId(), productDetailsDto.getId());
        Assertions.assertEquals(product.getName(), productDetailsDto.getName());
        Assertions.assertEquals(product.getDescription(), productDetailsDto.getDescription());
        Assertions.assertEquals(product.getPrice(), productDetailsDto.getPrice());
        Assertions.assertEquals(1, product.getIngredientProductAssociations().size());
        Assertions.assertEquals(1, productDetailsDto.getIngredients().size());
        Assertions.assertEquals(product.getIngredientProductAssociations().getFirst().getId().getIngredientId(), productDetailsDto.getIngredients().getFirst().getId());
        Assertions.assertEquals(product.getIngredientProductAssociations().getFirst().getIngredient().getName(), productDetailsDto.getIngredients().getFirst().getName());
        Assertions.assertEquals(product.getIngredientProductAssociations().getFirst().getQuantity(), productDetailsDto.getIngredients().getFirst().getQuantity());
        Assertions.assertEquals(product.getIngredientProductAssociations().getFirst().getQuantity() * product.getIngredientProductAssociations().getFirst().getIngredient().getCalories(), productDetailsDto.getCalories());
    }

    @Test
    void testMapToProduct() {
        Assertions.assertNull(productMapper.mapToProduct(null));

        val productCreationDto = ProductFixtures.getProductCreationDtoFixture();

        val product = productMapper.mapToProduct(productCreationDto);

        Assertions.assertNotNull(product);
        Assertions.assertEquals(productCreationDto.getName(), product.getName());
        Assertions.assertEquals(productCreationDto.getDescription(), product.getDescription());
        Assertions.assertEquals(productCreationDto.getPrice(), product.getPrice());
    }

    @Test
    void testMergeToProduct() {
        val product = ProductFixtures.getProductFixture();

        Assertions.assertDoesNotThrow(() -> productMapper.mergeToProduct(product, null));

        product.setName("old");
        product.setDescription("old");
        product.setPrice(0.);
        val productUpdateDto = ProductFixtures.getProductUpdateDtoFixture();

        productMapper.mergeToProduct(product, productUpdateDto);

        Assertions.assertEquals(productUpdateDto.getName(), product.getName());
        Assertions.assertEquals(productUpdateDto.getDescription(), product.getDescription());
        Assertions.assertEquals(productUpdateDto.getPrice(), product.getPrice());
    }
}
