package ro.unibuc.fmi.awbd.service.loyaltycard.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.fixtures.ClientFixtures;
import ro.unibuc.fmi.awbd.service.layaltycard.mapper.LoyaltyCardMapper;
import ro.unibuc.fmi.awbd.service.layaltycard.mapper.LoyaltyCardMapperImpl;

@SpringBootTest(classes = {LoyaltyCardMapperImpl.class})
class LoyaltyCardMapperTest {
    @Autowired
    LoyaltyCardMapper loyaltyCardMapper;

    @Test
    void testMapToLoyaltyCard() {
        Assertions.assertNull(loyaltyCardMapper.mapToLoyaltyCard(null));

        val client = ClientFixtures.getClientFixture();

        val loyaltyCard = loyaltyCardMapper.mapToLoyaltyCard(client);

        Assertions.assertNotNull(loyaltyCard);
        Assertions.assertNull(loyaltyCard.getId());
        Assertions.assertEquals(0, loyaltyCard.getPoints());
        Assertions.assertEquals(client, loyaltyCard.getClient());
    }
}
