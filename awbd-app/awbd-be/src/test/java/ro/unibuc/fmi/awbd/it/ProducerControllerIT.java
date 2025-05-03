package ro.unibuc.fmi.awbd.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.controller.models.ProducerDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ProducersPageDto;
import ro.unibuc.fmi.awbd.domain.producer.repository.ProducerRepository;
import ro.unibuc.fmi.awbd.fixtures.ProducerFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/producer_integration_test.sql")
@Transactional
class ProducerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProducerRepository producerRepository;

    @Test
    @SneakyThrows
    void whenGetProducersPage_thenGetProducersPage() {
        val requestParameters = new LinkedMultiValueMap<String, String>();
        requestParameters.set("sort", "+name");
        requestParameters.set("limit", "10");
        requestParameters.set("offset", "1");
        requestParameters.set("name", "Farm");
        requestParameters.set("address", "Str.");

        val response = mockMvc.perform(get("/producers")
                        .queryParams(requestParameters))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val producersPage = objectMapper.readValue(response.getResponse().getContentAsString(), ProducersPageDto.class);

        Assertions.assertNotNull(producersPage);
        Assertions.assertNotNull(producersPage.getPagination());
        Assertions.assertEquals(1, producersPage.getPagination().getPage());
        Assertions.assertEquals(10, producersPage.getPagination().getPageSize());
        Assertions.assertEquals(2, producersPage.getPagination().getItemsTotal());
        Assertions.assertEquals("+name", producersPage.getPagination().getSort());
        Assertions.assertEquals(1, producersPage.getPagination().getPagesTotal());
        Assertions.assertFalse(producersPage.getPagination().getHasNextPage());
        Assertions.assertNotNull(producersPage.getItems());
        Assertions.assertEquals(2, producersPage.getItems().size());
        Assertions.assertEquals(2, producersPage.getItems().getFirst().getId());
        Assertions.assertEquals("Better Farm", producersPage.getItems().getFirst().getName());
        Assertions.assertEquals("Str. 1 Nr. 2", producersPage.getItems().getFirst().getAddress());
        Assertions.assertEquals(1, producersPage.getItems().get(1).getId());
        Assertions.assertEquals("Good Farm", producersPage.getItems().get(1).getName());
        Assertions.assertEquals("Str. 1 Nr. 1", producersPage.getItems().get(1).getAddress());
    }

    @Test
    @SneakyThrows
    void whenGetProducers_thenGetProducers() {
        mockMvc.perform(get("/producers/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Better Farm"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].name").value("Good Farm"));
    }

    @Test
    @SneakyThrows
    void whenGetProducerDetails_thenGetProducerDetails() {
        val response = mockMvc.perform(get("/producers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val producerDetails = objectMapper.readValue(response.getResponse().getContentAsString(), ProducerDetailsDto.class);

        Assertions.assertEquals(1, producerDetails.getId());
        Assertions.assertEquals("Good Farm", producerDetails.getName());
        Assertions.assertEquals("Str. 1 Nr. 1", producerDetails.getAddress());
    }

    @Test
    @SneakyThrows
    void whenCreateProducer_thenCreateProducer() {
        val producerCreationDto = ProducerFixtures.getProducerCreationDtoFixture();

        Assertions.assertTrue(producerRepository.findByNameAndAddress(producerCreationDto.getName(), producerCreationDto.getAddress()).isEmpty());

        mockMvc.perform(post("/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producerCreationDto)))
                .andExpect(status().isCreated());

        val createdProducerOptional = producerRepository.findByNameAndAddress(producerCreationDto.getName(), producerCreationDto.getAddress());

        Assertions.assertTrue(createdProducerOptional.isPresent());
        Assertions.assertNotNull(createdProducerOptional.get().getId());
        Assertions.assertEquals(producerCreationDto.getName(), createdProducerOptional.get().getName());
        Assertions.assertEquals(producerCreationDto.getAddress(), createdProducerOptional.get().getAddress());
    }

    @Test
    @SneakyThrows
    void whenUpdateProducer_thenUpdateProducer() {
        val producerUpdateDto = ProducerFixtures.getProducerUpdateDtoFixture();

        mockMvc.perform(put("/producers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producerUpdateDto)))
                .andExpect(status().isOk());

        val updatedProducerOptional = producerRepository.findById(1L);

        Assertions.assertTrue(updatedProducerOptional.isPresent());
        Assertions.assertEquals(1L, updatedProducerOptional.get().getId());
        Assertions.assertEquals(producerUpdateDto.getName(), updatedProducerOptional.get().getName());
        Assertions.assertEquals(producerUpdateDto.getAddress(), updatedProducerOptional.get().getAddress());
    }

    @Test
    @SneakyThrows
    void whenDeleteProducer_thenDeleteProducer() {
        mockMvc.perform(delete("/producers/1"))
                .andExpect(status().isOk());

        Assertions.assertTrue(producerRepository.findById(1L).isEmpty());
    }
}
