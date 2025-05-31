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
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.controller.models.ClientDetailsDto;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.domain.user.repository.client.ClientRepository;
import ro.unibuc.fmi.awbd.fixtures.ClientFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/client_integration_test.sql")
@Transactional
class ClientControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "CLIENT")
    void whenGetClientDetails_thenGetClientDetails() {
        val response = mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        val clientDetails = objectMapper.readValue(response.getResponse().getContentAsString(), ClientDetailsDto.class);

        Assertions.assertNotNull(clientDetails);
        Assertions.assertEquals(1, clientDetails.getId());
        Assertions.assertEquals("client@client.com", clientDetails.getEmail());
        Assertions.assertEquals("Client Client", clientDetails.getName());
        Assertions.assertEquals("0712345678", clientDetails.getPhoneNumber());
    }

    @Test
    @SneakyThrows
    void whenCreateClient_thenCreateClient() {
        val clientCreationDto = ClientFixtures.getClientCreationDtoFixture();

        Assertions.assertTrue(clientRepository.findByEmail(clientCreationDto.getEmail()).isEmpty());

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientCreationDto)))
                .andExpect(status().isCreated());

        val createdClientOptional = clientRepository.findByEmail(clientCreationDto.getEmail());

        Assertions.assertTrue(createdClientOptional.isPresent());
        Assertions.assertNotNull(createdClientOptional.get().getId());
        Assertions.assertEquals(clientCreationDto.getEmail(), createdClientOptional.get().getEmail());
        Assertions.assertNotNull(createdClientOptional.get().getPassword());
        Assertions.assertEquals(clientCreationDto.getName(), createdClientOptional.get().getName());
        Assertions.assertEquals(Role.CLIENT, createdClientOptional.get().getRole());
        Assertions.assertEquals(clientCreationDto.getPhoneNumber(), createdClientOptional.get().getPhoneNumber());
        Assertions.assertNull(createdClientOptional.get().getLoyaltyCard());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "CLIENT")
    void whenUpdateClient_thenUpdateClient() {
        val clientUpdateDto = ClientFixtures.getClientUpdateDtoFixture();

        val oldClientOptional = clientRepository.findById(1L);

        Assertions.assertTrue(oldClientOptional.isPresent());

        val oldEmail = oldClientOptional.get().getEmail();
        val oldPassword = oldClientOptional.get().getPassword();
        val oldLoyaltyCard = oldClientOptional.get().getLoyaltyCard();

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientUpdateDto)))
                .andExpect(status().isOk());

        val updatedClientOptional = clientRepository.findById(1L);

        Assertions.assertTrue(updatedClientOptional.isPresent());
        Assertions.assertEquals(1L, updatedClientOptional.get().getId());
        Assertions.assertEquals(oldEmail, updatedClientOptional.get().getEmail());
        Assertions.assertNotEquals(oldPassword, updatedClientOptional.get().getPassword());
        Assertions.assertEquals(clientUpdateDto.getName(), updatedClientOptional.get().getName());
        Assertions.assertEquals(Role.CLIENT, updatedClientOptional.get().getRole());
        Assertions.assertEquals(clientUpdateDto.getPhoneNumber(), updatedClientOptional.get().getPhoneNumber());
        Assertions.assertEquals(oldLoyaltyCard, updatedClientOptional.get().getLoyaltyCard());
    }
}
