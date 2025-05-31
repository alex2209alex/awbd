package ro.unibuc.fmi.awbd.service.layaltycard;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;
import ro.unibuc.fmi.awbd.domain.user.repository.client.ClientRepository;
import ro.unibuc.fmi.awbd.service.layaltycard.mapper.LoyaltyCardMapper;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

@Service
@RequiredArgsConstructor
public class LoyaltyCardService {
    private final LoyaltyCardMapper loyaltyCardMapper;
    private final UserInformationService userInformationService;
    private final ClientRepository clientRepository;

    @Transactional
    public void createLoyaltyCard() {
        userInformationService.ensureCurrentUserIsClient();
        val client = (Client) userInformationService.getCurrentUser();
        if (client.getLoyaltyCard() != null) {
            throw new ForbiddenException(ErrorMessageUtils.USER_HAS_LOYALTY_CARD);
        }
        val loyaltyCard = loyaltyCardMapper.mapToLoyaltyCard(client);
        client.setLoyaltyCard(loyaltyCard);
        clientRepository.save(client);
    }
}
