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
import ro.unibuc.fmi.awbd.controller.models.IngredientDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.IngredientsPageDto;
import ro.unibuc.fmi.awbd.domain.ingredient.repository.IngredientRepository;
import ro.unibuc.fmi.awbd.fixtures.IngredientFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/ingredient_integration_test.sql")
@Transactional
class IngredientControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetIngredientsPage_thenGetIngredientsPage() {
        val requestParameters = new LinkedMultiValueMap<String, String>();
        requestParameters.set("sort", "+name");
        requestParameters.set("limit", "10");
        requestParameters.set("offset", "1");
        requestParameters.set("name", "ing");
        requestParameters.set("producer", "pro");

        val response = mockMvc.perform(get("/ingredients")
                        .queryParams(requestParameters))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val ingredientsPage = objectMapper.readValue(response.getResponse().getContentAsString(), IngredientsPageDto.class);

        Assertions.assertNotNull(ingredientsPage);
        Assertions.assertNotNull(ingredientsPage.getPagination());
        Assertions.assertEquals(1, ingredientsPage.getPagination().getPage());
        Assertions.assertEquals(10, ingredientsPage.getPagination().getPageSize());
        Assertions.assertEquals(1, ingredientsPage.getPagination().getItemsTotal());
        Assertions.assertEquals("+name", ingredientsPage.getPagination().getSort());
        Assertions.assertEquals(1, ingredientsPage.getPagination().getPagesTotal());
        Assertions.assertFalse(ingredientsPage.getPagination().getHasNextPage());
        Assertions.assertNotNull(ingredientsPage.getItems());
        Assertions.assertEquals(1, ingredientsPage.getItems().size());
        Assertions.assertEquals(1, ingredientsPage.getItems().getFirst().getId());
        Assertions.assertEquals("Ingredient Name", ingredientsPage.getItems().getFirst().getName());
        Assertions.assertEquals("Producer Name", ingredientsPage.getItems().getFirst().getProducer());
        Assertions.assertEquals(10., ingredientsPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(100., ingredientsPage.getItems().getFirst().getCalories());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetIngredients_thenGetIngredients() {
        mockMvc.perform(get("/ingredients/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ingredient Name"));
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetIngredientDetails_thenGetIngredientDetails() {
        val response = mockMvc.perform(get("/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val ingredientDetails = objectMapper.readValue(response.getResponse().getContentAsString(), IngredientDetailsDto.class);

        Assertions.assertEquals(1, ingredientDetails.getId());
        Assertions.assertEquals("Ingredient Name", ingredientDetails.getName());
        Assertions.assertEquals(10., ingredientDetails.getPrice());
        Assertions.assertEquals(100., ingredientDetails.getCalories());
        Assertions.assertEquals(1L, ingredientDetails.getProducerId());
        Assertions.assertEquals("Producer Name", ingredientDetails.getProducerName());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenCreateIngredient_thenCreateIngredient() {
        val ingredientCreationDto = IngredientFixtures.getIngredientCreationDtoFixture();
        ingredientCreationDto.setProducerId(1L);

        mockMvc.perform(post("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredientCreationDto)))
                .andExpect(status().isCreated());

        val createdIngredientOptional = ingredientRepository.findAll().stream().filter(ingredient -> ingredient.getName().equals(ingredientCreationDto.getName())).findAny();

        Assertions.assertTrue(createdIngredientOptional.isPresent());
        Assertions.assertNotNull(createdIngredientOptional.get().getId());
        Assertions.assertEquals(ingredientCreationDto.getName(), createdIngredientOptional.get().getName());
        Assertions.assertEquals(ingredientCreationDto.getPrice(), createdIngredientOptional.get().getPrice());
        Assertions.assertEquals(ingredientCreationDto.getCalories(), createdIngredientOptional.get().getCalories());
        Assertions.assertEquals(ingredientCreationDto.getProducerId(), createdIngredientOptional.get().getProducer().getId());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenUpdateIngredient_thenUpdateIngredient() {
        val ingredientUpdateDto = IngredientFixtures.getIngredientUpdateDtoFixture();
        ingredientUpdateDto.setProducerId(2L);

        mockMvc.perform(put("/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredientUpdateDto)))
                .andExpect(status().isOk());

        val updatedIngredientOptional = ingredientRepository.findById(1L);

        Assertions.assertTrue(updatedIngredientOptional.isPresent());
        Assertions.assertEquals(1L, updatedIngredientOptional.get().getId());
        Assertions.assertEquals(ingredientUpdateDto.getName(), updatedIngredientOptional.get().getName());
        Assertions.assertEquals(ingredientUpdateDto.getPrice(), updatedIngredientOptional.get().getPrice());
        Assertions.assertEquals(ingredientUpdateDto.getCalories(), updatedIngredientOptional.get().getCalories());
        Assertions.assertEquals(ingredientUpdateDto.getProducerId(), updatedIngredientOptional.get().getProducer().getId());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenDeleteIngredient_thenDeleteIngredient() {
        mockMvc.perform(delete("/ingredients/1"))
                .andExpect(status().isOk());

        Assertions.assertTrue(ingredientRepository.findById(1L).isEmpty());
    }
}
