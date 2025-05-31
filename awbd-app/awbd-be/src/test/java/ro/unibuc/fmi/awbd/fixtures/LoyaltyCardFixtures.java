package ro.unibuc.fmi.awbd.fixtures;

import ro.unibuc.fmi.awbd.domain.loyaltycard.model.LoyaltyCard;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;

public class LoyaltyCardFixtures {
    private LoyaltyCardFixtures() {
    }

    public static LoyaltyCard getLoyaltyCardFixture(Client client) {
        LoyaltyCard loyaltyCard = new LoyaltyCard();
        loyaltyCard.setId(client.getId());
        loyaltyCard.setPoints(0L);
        loyaltyCard.setClient(client);
        return loyaltyCard;
    }
}
