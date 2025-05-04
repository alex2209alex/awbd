package ro.unibuc.fmi.awbd.service.layaltycard.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.fmi.awbd.domain.loyaltycard.model.LoyaltyCard;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LoyaltyCardMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "points", constant = "0L")
    LoyaltyCard mapLoyaltyCard(Client client);
}
