package ro.unibuc.fmi.awbd.it;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductSearchRepository;
import ro.unibuc.fmi.awbd.service.product.model.ProductFilter;

@IntegrationTest
@Sql(scripts = {"/db/product_search_repository_test.sql"})
@Transactional
class ProductSearchRepositoryIT {
    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Test
    void givenNoSearchParameters_whenGetProductsPage_thenReturnProductsPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("-price")
                .build();
        val filter = new ProductFilter();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        var productsPage = productSearchRepository.getProductsPage(pageRequest);

        Assertions.assertNotNull(productsPage);
        Assertions.assertNotNull(productsPage.getItems());
        Assertions.assertEquals(2, productsPage.getItems().size());
        Assertions.assertEquals(2, productsPage.getItems().getFirst().getId());
        Assertions.assertEquals("Product 2", productsPage.getItems().getFirst().getName());
        Assertions.assertEquals("Product description 2", productsPage.getItems().getFirst().getDescription());
        Assertions.assertEquals(200., productsPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(100., productsPage.getItems().getFirst().getCalories());
        Assertions.assertEquals(1, productsPage.getItems().get(1).getId());
        Assertions.assertEquals("Product 1", productsPage.getItems().get(1).getName());
        Assertions.assertEquals("Product description", productsPage.getItems().get(1).getDescription());
        Assertions.assertEquals(100., productsPage.getItems().get(1).getPrice());
        Assertions.assertEquals(500., productsPage.getItems().get(1).getCalories());

        paginationRequest.setSort("-description");

        productsPage = productSearchRepository.getProductsPage(pageRequest);

        Assertions.assertNotNull(productsPage);
        Assertions.assertNotNull(productsPage.getItems());
        Assertions.assertEquals(2, productsPage.getItems().size());
        Assertions.assertEquals(2, productsPage.getItems().getFirst().getId());
        Assertions.assertEquals(1, productsPage.getItems().get(1).getId());

        paginationRequest.setSort("+calories");

        productsPage = productSearchRepository.getProductsPage(pageRequest);

        Assertions.assertNotNull(productsPage);
        Assertions.assertNotNull(productsPage.getItems());
        Assertions.assertEquals(2, productsPage.getItems().size());
        Assertions.assertEquals(2, productsPage.getItems().getFirst().getId());
        Assertions.assertEquals(1, productsPage.getItems().get(1).getId());
    }

    @Test
    void givenSearchParameters_whenGetProductsPage_thenReturnProductsPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("+name")
                .build();
        val filter = ProductFilter.builder()
                .name("Product 1")
                .description("Product description")
                .build();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        val productsPage = productSearchRepository.getProductsPage(pageRequest);

        Assertions.assertNotNull(productsPage);
        Assertions.assertNotNull(productsPage.getItems());
        Assertions.assertEquals(1, productsPage.getItems().size());
        Assertions.assertEquals(1, productsPage.getItems().getFirst().getId());
        Assertions.assertEquals("Product 1", productsPage.getItems().getFirst().getName());
        Assertions.assertEquals("Product description", productsPage.getItems().getFirst().getDescription());
        Assertions.assertEquals(100., productsPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(500., productsPage.getItems().getFirst().getCalories());
    }
}
