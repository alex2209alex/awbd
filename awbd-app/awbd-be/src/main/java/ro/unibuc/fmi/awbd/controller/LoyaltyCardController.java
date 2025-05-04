package ro.unibuc.fmi.awbd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.awbd.common.utils.LogUtils;
import ro.unibuc.fmi.awbd.controller.api.LoyaltyCardApi;
import ro.unibuc.fmi.awbd.service.layaltycard.LoyaltyCardService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoyaltyCardController implements LoyaltyCardApi {
    private final LoyaltyCardService loyaltyCardService;

    @Override
    public void createLoyaltyCard(
    ) {
        log.info(LogUtils.CREATE_LOYALTY_CARD_REQUEST);
        loyaltyCardService.createLoyaltyCard();
    }
}
