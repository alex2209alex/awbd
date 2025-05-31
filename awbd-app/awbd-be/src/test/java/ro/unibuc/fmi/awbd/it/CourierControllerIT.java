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
import ro.unibuc.fmi.awbd.controller.models.CourierDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CouriersPageDto;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.domain.user.repository.courier.CourierRepository;
import ro.unibuc.fmi.awbd.fixtures.CourierFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/courier_integration_test.sql")
@Transactional
class CourierControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourierRepository courierRepository;

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetCouriersPage_thenGetCouriersPage() {
        val requestParameters = new LinkedMultiValueMap<String, String>();
        requestParameters.set("sort", "+name");
        requestParameters.set("limit", "10");
        requestParameters.set("offset", "1");
        requestParameters.set("email", "courier@co");
        requestParameters.set("name", "Courier");

        val response = mockMvc.perform(get("/couriers")
                        .queryParams(requestParameters))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val couriersPage = objectMapper.readValue(response.getResponse().getContentAsString(), CouriersPageDto.class);

        Assertions.assertNotNull(couriersPage);
        Assertions.assertNotNull(couriersPage.getPagination());
        Assertions.assertEquals(1, couriersPage.getPagination().getPage());
        Assertions.assertEquals(10, couriersPage.getPagination().getPageSize());
        Assertions.assertEquals(1, couriersPage.getPagination().getItemsTotal());
        Assertions.assertEquals("+name", couriersPage.getPagination().getSort());
        Assertions.assertEquals(1, couriersPage.getPagination().getPagesTotal());
        Assertions.assertFalse(couriersPage.getPagination().getHasNextPage());
        Assertions.assertNotNull(couriersPage.getItems());
        Assertions.assertEquals(1, couriersPage.getItems().size());
        Assertions.assertEquals(2, couriersPage.getItems().getFirst().getId());
        Assertions.assertEquals("courier@courier.com", couriersPage.getItems().getFirst().getEmail());
        Assertions.assertEquals("Courier", couriersPage.getItems().getFirst().getName());
        Assertions.assertEquals("0712345678", couriersPage.getItems().getFirst().getPhoneNumber());
        Assertions.assertEquals(2000., couriersPage.getItems().getFirst().getSalary());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenGetCourierDetails_thenGetCourierDetails() {
        val response = mockMvc.perform(get("/couriers/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val courierDetails = objectMapper.readValue(response.getResponse().getContentAsString(), CourierDetailsDto.class);

        Assertions.assertEquals(2, courierDetails.getId());
        Assertions.assertEquals("courier@courier.com", courierDetails.getEmail());
        Assertions.assertEquals("Courier", courierDetails.getName());
        Assertions.assertEquals("0712345678", courierDetails.getPhoneNumber());
        Assertions.assertEquals(2000., courierDetails.getSalary());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenCreateCourier_thenCreateCourier() {
        val courierCreationDto = CourierFixtures.getCourierCreationDtoFixture();

        Assertions.assertTrue(courierRepository.findByEmail(courierCreationDto.getEmail()).isEmpty());

        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courierCreationDto)))
                .andExpect(status().isCreated());

        val createdCourierOptional = courierRepository.findByEmail(courierCreationDto.getEmail());

        Assertions.assertTrue(createdCourierOptional.isPresent());
        Assertions.assertNotNull(createdCourierOptional.get().getId());
        Assertions.assertEquals(courierCreationDto.getEmail(), createdCourierOptional.get().getEmail());
        Assertions.assertEquals(courierCreationDto.getName(), createdCourierOptional.get().getName());
        Assertions.assertNotNull(createdCourierOptional.get().getPassword());
        Assertions.assertEquals(Role.COURIER, createdCourierOptional.get().getRole());
        Assertions.assertEquals(courierCreationDto.getPhoneNumber(), createdCourierOptional.get().getPhoneNumber());
        Assertions.assertEquals(courierCreationDto.getSalary(), createdCourierOptional.get().getSalary());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenUpdateCourier_thenUpdateCourier() {
        val courierUpdateDto = CourierFixtures.getCourierUpdateDtoFixture();

        val oldCourierOptional = courierRepository.findById(2L);

        Assertions.assertTrue(oldCourierOptional.isPresent());

        val oldEmail = oldCourierOptional.get().getEmail();
        val oldPassword = oldCourierOptional.get().getPassword();

        mockMvc.perform(put("/couriers/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courierUpdateDto)))
                .andExpect(status().isOk());

        val updatedCourierOptional = courierRepository.findById(2L);

        Assertions.assertTrue(updatedCourierOptional.isPresent());
        Assertions.assertEquals(2, updatedCourierOptional.get().getId());
        Assertions.assertEquals(oldEmail, updatedCourierOptional.get().getEmail());
        Assertions.assertEquals(courierUpdateDto.getName(), updatedCourierOptional.get().getName());
        Assertions.assertNotEquals(oldPassword, updatedCourierOptional.get().getPassword());
        Assertions.assertEquals(Role.COURIER, updatedCourierOptional.get().getRole());
        Assertions.assertEquals(courierUpdateDto.getPhoneNumber(), updatedCourierOptional.get().getPhoneNumber());
        Assertions.assertEquals(courierUpdateDto.getSalary(), updatedCourierOptional.get().getSalary());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenDeleteCourier_thenDeleteCourier() {
        mockMvc.perform(delete("/couriers/3"))
                .andExpect(status().isOk());

        Assertions.assertTrue(courierRepository.findById(3L).isEmpty());
    }
}
