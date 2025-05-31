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
import ro.unibuc.fmi.awbd.controller.models.TokenResponseDto;
import ro.unibuc.fmi.awbd.fixtures.UserFixtures;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/user_integration_test.sql")
@Transactional
class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "RESTAURANT_ADMIN")
    void whenLoginUser_thenLoginUser() {
        val userLoginDto = UserFixtures.getUserLoginDtoFixture("restaurant@admin.com", "123456");

        val response = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk())
                .andReturn();

        val tokenResponse = objectMapper.readValue(response.getResponse().getContentAsString(), TokenResponseDto.class);

        Assertions.assertNotNull(tokenResponse);
        Assertions.assertNotNull(tokenResponse.getToken());
    }
}
