package ro.unibuc.fmi.awbd.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.controller.models.ProductDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ProductIngredientCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ProductIngredientUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.ProductsPageDto;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductRepository;
import ro.unibuc.fmi.awbd.fixtures.ProductFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/product_integration_test.sql")
@Transactional
class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetProductsPage_thenGetProductsPage() {
        val requestParameters = new LinkedMultiValueMap<String, String>();
        requestParameters.set("sort", "+name");
        requestParameters.set("limit", "10");
        requestParameters.set("offset", "1");
        requestParameters.set("name", "Product 2");
        requestParameters.set("description", "Product descrip");

        val response = mockMvc.perform(get("/products")
                        .queryParams(requestParameters))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val productsPage = objectMapper.readValue(response.getResponse().getContentAsString(), ProductsPageDto.class);

        Assertions.assertNotNull(productsPage);
        Assertions.assertNotNull(productsPage.getPagination());
        Assertions.assertEquals(1, productsPage.getPagination().getPage());
        Assertions.assertEquals(10, productsPage.getPagination().getPageSize());
        Assertions.assertEquals(1, productsPage.getPagination().getItemsTotal());
        Assertions.assertEquals("+name", productsPage.getPagination().getSort());
        Assertions.assertEquals(1, productsPage.getPagination().getPagesTotal());
        Assertions.assertFalse(productsPage.getPagination().getHasNextPage());
        Assertions.assertNotNull(productsPage.getItems());
        Assertions.assertEquals(1, productsPage.getItems().size());
        Assertions.assertEquals(2, productsPage.getItems().getFirst().getId());
        Assertions.assertEquals("Product 2", productsPage.getItems().getFirst().getName());
        Assertions.assertEquals("Product description 2", productsPage.getItems().getFirst().getDescription());
        Assertions.assertEquals(200., productsPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(2000., productsPage.getItems().getFirst().getCalories());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetProducts_thenGetProducts() {
        mockMvc.perform(get("/products/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Product"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetProductDetails_thenGetProductDetails() {
        val response = mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val productDetails = objectMapper.readValue(response.getResponse().getContentAsString(), ProductDetailsDto.class);

        Assertions.assertEquals(1, productDetails.getId());
        Assertions.assertEquals("Product", productDetails.getName());
        Assertions.assertEquals("Product description", productDetails.getDescription());
        Assertions.assertEquals(100., productDetails.getPrice());
        Assertions.assertEquals(1000., productDetails.getCalories());
        Assertions.assertEquals(1, productDetails.getIngredients().size());
        Assertions.assertEquals(1, productDetails.getIngredients().getFirst().getId());
        Assertions.assertEquals("Ingredient", productDetails.getIngredients().getFirst().getName());
        Assertions.assertEquals(100., productDetails.getIngredients().getFirst().getQuantity());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenCreateProduct_thenCreateProduct() {
        val productCreationDto = ProductFixtures.getProductCreationDtoFixture();
        productCreationDto.setIngredients(List.of(new ProductIngredientCreationDto(1L, 1000.)));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreationDto)))
                .andExpect(status().isCreated());

        val createdProductOptional = productRepository.findAll().stream().filter(product -> product.getName().equals(productCreationDto.getName())).findAny();

        Assertions.assertTrue(createdProductOptional.isPresent());
        Assertions.assertNotNull(createdProductOptional.get().getId());
        Assertions.assertEquals(productCreationDto.getName(), createdProductOptional.get().getName());
        Assertions.assertEquals(productCreationDto.getDescription(), createdProductOptional.get().getDescription());
        Assertions.assertEquals(productCreationDto.getPrice(), createdProductOptional.get().getPrice());
        Assertions.assertEquals(1, createdProductOptional.get().getIngredientProductAssociations().size());
        Assertions.assertNotNull(createdProductOptional.get().getIngredientProductAssociations().getFirst().getId().getProductId());
        Assertions.assertEquals(1, createdProductOptional.get().getIngredientProductAssociations().getFirst().getId().getIngredientId());
        Assertions.assertEquals(1000., createdProductOptional.get().getIngredientProductAssociations().getFirst().getQuantity());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenUpdateProduct_thenUpdateProduct() {
        val productUpdateDto = ProductFixtures.getProductUpdateDtoFixture();
        productUpdateDto.setIngredients(List.of(new ProductIngredientUpdateDto(2L, 1000.)));

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateDto)))
                .andExpect(status().isOk());

        val updatedProductOptional = productRepository.findById(1L);

        Assertions.assertTrue(updatedProductOptional.isPresent());
        Assertions.assertEquals(1L, updatedProductOptional.get().getId());
        Assertions.assertEquals(productUpdateDto.getName(), updatedProductOptional.get().getName());
        Assertions.assertEquals(productUpdateDto.getDescription(), updatedProductOptional.get().getDescription());
        Assertions.assertEquals(productUpdateDto.getPrice(), updatedProductOptional.get().getPrice());
        Assertions.assertEquals(1, updatedProductOptional.get().getIngredientProductAssociations().size());
        Assertions.assertEquals(1, updatedProductOptional.get().getIngredientProductAssociations().getFirst().getId().getProductId());
        Assertions.assertEquals(2, updatedProductOptional.get().getIngredientProductAssociations().getFirst().getId().getIngredientId());
        Assertions.assertEquals(1000., updatedProductOptional.get().getIngredientProductAssociations().getFirst().getQuantity());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenDeleteProduct_thenDeleteProduct() {
        mockMvc.perform(delete("/products/2"))
                .andExpect(status().isOk());

        Assertions.assertTrue(productRepository.findById(2L).isEmpty());
    }
}
