package ro.unibuc.fmi.awbd.it;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.domain.loyaltycard.repository.LoyaltyCardRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@IntegrationTest
@Sql("/db/loyalty_card_integration_test.sql")
@Transactional
class LoyaltyCardControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;

    @Test
    @SneakyThrows
    @WithMockUser(username = "1", authorities = "CLIENT")
    void whenCreateLoyaltyCard_thenCreateLoyaltyCard() {
        Assertions.assertTrue(loyaltyCardRepository.findById(1L).isEmpty());

        mockMvc.perform(post("/loyalty-cards"))
                .andExpect(status().isCreated());

        val createdLoyaltyCardOptional = loyaltyCardRepository.findById(1L);

        Assertions.assertTrue(createdLoyaltyCardOptional.isPresent());
        Assertions.assertEquals(1, createdLoyaltyCardOptional.get().getId());
        Assertions.assertEquals(0, createdLoyaltyCardOptional.get().getPoints());
    }
}
