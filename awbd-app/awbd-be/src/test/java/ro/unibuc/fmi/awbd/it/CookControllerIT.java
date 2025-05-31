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
import ro.unibuc.fmi.awbd.controller.models.CookDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CookProductCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CookProductUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.CooksPageDto;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.domain.user.repository.cook.CookRepository;
import ro.unibuc.fmi.awbd.fixtures.CookFixtures;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/cook_integration_test.sql")
@Transactional
class CookControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CookRepository cookRepository;

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetCooksPage_thenGetCooksPage() {
        val requestParameters = new LinkedMultiValueMap<String, String>();
        requestParameters.set("sort", "+name");
        requestParameters.set("limit", "10");
        requestParameters.set("offset", "1");
        requestParameters.set("email", "cook@co");
        requestParameters.set("name", "Cook");

        val response = mockMvc.perform(get("/cooks")
                        .queryParams(requestParameters))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val cooksPage = objectMapper.readValue(response.getResponse().getContentAsString(), CooksPageDto.class);

        Assertions.assertNotNull(cooksPage);
        Assertions.assertNotNull(cooksPage.getPagination());
        Assertions.assertEquals(1, cooksPage.getPagination().getPage());
        Assertions.assertEquals(10, cooksPage.getPagination().getPageSize());
        Assertions.assertEquals(1, cooksPage.getPagination().getItemsTotal());
        Assertions.assertEquals("+name", cooksPage.getPagination().getSort());
        Assertions.assertEquals(1, cooksPage.getPagination().getPagesTotal());
        Assertions.assertFalse(cooksPage.getPagination().getHasNextPage());
        Assertions.assertNotNull(cooksPage.getItems());
        Assertions.assertEquals(1, cooksPage.getItems().size());
        Assertions.assertEquals(2, cooksPage.getItems().getFirst().getId());
        Assertions.assertEquals("cook@cook.com", cooksPage.getItems().getFirst().getEmail());
        Assertions.assertEquals("Cook", cooksPage.getItems().getFirst().getName());
        Assertions.assertEquals(2000., cooksPage.getItems().getFirst().getSalary());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetCookDetails_thenGetCookDetails() {
        val response = mockMvc.perform(get("/cooks/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val cookDetails = objectMapper.readValue(response.getResponse().getContentAsString(), CookDetailsDto.class);

        Assertions.assertEquals(2, cookDetails.getId());
        Assertions.assertEquals("cook@cook.com", cookDetails.getEmail());
        Assertions.assertEquals("Cook", cookDetails.getName());
        Assertions.assertEquals(2000., cookDetails.getSalary());
        Assertions.assertEquals(1, cookDetails.getProducts().size());
        Assertions.assertEquals(1, cookDetails.getProducts().getFirst().getId());
        Assertions.assertEquals("Product", cookDetails.getProducts().getFirst().getName());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenCreateCook_thenCreateCook() {
        val cookCreationDto = CookFixtures.getCookCreationDtoFixture();
        cookCreationDto.setProducts(List.of(new CookProductCreationDto(1L)));

        Assertions.assertTrue(cookRepository.findByEmail(cookCreationDto.getEmail()).isEmpty());

        mockMvc.perform(post("/cooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cookCreationDto)))
                .andExpect(status().isCreated());

        val createdCookOptional = cookRepository.findByEmail(cookCreationDto.getEmail());

        Assertions.assertTrue(createdCookOptional.isPresent());
        Assertions.assertNotNull(createdCookOptional.get().getId());
        Assertions.assertEquals(cookCreationDto.getEmail(), createdCookOptional.get().getEmail());
        Assertions.assertEquals(cookCreationDto.getName(), createdCookOptional.get().getName());
        Assertions.assertNotNull(createdCookOptional.get().getPassword());
        Assertions.assertEquals(Role.COOK, createdCookOptional.get().getRole());
        Assertions.assertEquals(cookCreationDto.getSalary(), createdCookOptional.get().getSalary());
        Assertions.assertEquals(1, createdCookOptional.get().getProducts().size());
        Assertions.assertEquals(1, createdCookOptional.get().getProducts().getFirst().getId());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenUpdateCook_thenUpdateCook() {
        val cookUpdateDto = CookFixtures.getCookUpdateDtoFixture();
        cookUpdateDto.setProducts(List.of(new CookProductUpdateDto(2L)));

        val oldCookOptional = cookRepository.findById(2L);

        Assertions.assertTrue(oldCookOptional.isPresent());

        val oldEmail = oldCookOptional.get().getEmail();
        val oldPassword = oldCookOptional.get().getPassword();

        mockMvc.perform(put("/cooks/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cookUpdateDto)))
                .andExpect(status().isOk());

        val updatedCookOptional = cookRepository.findById(2L);

        Assertions.assertTrue(updatedCookOptional.isPresent());
        Assertions.assertEquals(2, updatedCookOptional.get().getId());
        Assertions.assertEquals(oldEmail, updatedCookOptional.get().getEmail());
        Assertions.assertEquals(cookUpdateDto.getName(), updatedCookOptional.get().getName());
        Assertions.assertNotEquals(oldPassword, updatedCookOptional.get().getPassword());
        Assertions.assertEquals(Role.COOK, updatedCookOptional.get().getRole());
        Assertions.assertEquals(cookUpdateDto.getSalary(), updatedCookOptional.get().getSalary());
        Assertions.assertEquals(1, updatedCookOptional.get().getProducts().size());
        Assertions.assertEquals(2, updatedCookOptional.get().getProducts().getFirst().getId());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenDeleteCook_thenDeleteCook() {
        mockMvc.perform(delete("/cooks/3"))
                .andExpect(status().isOk());

        Assertions.assertTrue(cookRepository.findById(3L).isEmpty());
    }
}
