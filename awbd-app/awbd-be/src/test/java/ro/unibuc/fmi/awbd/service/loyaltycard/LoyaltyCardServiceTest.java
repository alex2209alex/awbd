package ro.unibuc.fmi.awbd.service.loyaltycard;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.domain.loyaltycard.model.LoyaltyCard;
import ro.unibuc.fmi.awbd.domain.user.repository.client.ClientRepository;
import ro.unibuc.fmi.awbd.fixtures.ClientFixtures;
import ro.unibuc.fmi.awbd.fixtures.LoyaltyCardFixtures;
import ro.unibuc.fmi.awbd.service.layaltycard.LoyaltyCardService;
import ro.unibuc.fmi.awbd.service.layaltycard.mapper.LoyaltyCardMapper;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

@ExtendWith(MockitoExtension.class)
class LoyaltyCardServiceTest {
    @Mock
    LoyaltyCardMapper loyaltyCardMapper;

    @Mock
    UserInformationService userInformationService;

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    LoyaltyCardService loyaltyCardService;

    @Test
    void givenUserHasLoyaltyCard_whenCreateLoyaltyCard_thenForbiddenException() {
        val client = ClientFixtures.getClientFixture();
        client.setLoyaltyCard(new LoyaltyCard());

        Mockito.when(userInformationService.getCurrentUser()).thenReturn(client);

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> loyaltyCardService.createLoyaltyCard());

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("User has loyalty card", exc.getMessage());
    }

    @Test
    void whenCreateLoyaltyCard_thenCreateLoyaltyCard() {
        val client = ClientFixtures.getClientFixture();
        client.setLoyaltyCard(null);
        val loyaltyCard = LoyaltyCardFixtures.getLoyaltyCardFixture(client);

        Mockito.when(userInformationService.getCurrentUser()).thenReturn(client);
        Mockito.when(loyaltyCardMapper.mapToLoyaltyCard(client)).thenReturn(loyaltyCard);

        Assertions.assertDoesNotThrow(() -> loyaltyCardService.createLoyaltyCard());

        Assertions.assertEquals(client.getLoyaltyCard(), loyaltyCard);

        Mockito.verify(clientRepository, Mockito.times(1)).save(client);
    }
}
