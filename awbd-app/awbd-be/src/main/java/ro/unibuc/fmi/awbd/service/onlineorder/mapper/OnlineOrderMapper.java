package ro.unibuc.fmi.awbd.service.onlineorder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderCreationDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderProductDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrdersPageDto;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrder;
import ro.unibuc.fmi.awbd.domain.product.model.ProductOnlineOrderAssociation;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;
import ro.unibuc.fmi.awbd.service.onlineorder.model.OnlineOrderPageElementDetails;

import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OnlineOrderMapper {
    @Mapping(target = "pagination", source = "paginationInformation")
    OnlineOrdersPageDto mapToOnlineOrdersPageDto(Page<OnlineOrderPageElementDetails> page);

    @Mapping(target = "clientEmail", source = "client.email")
    @Mapping(target = "clientPhoneNumber", source = "client.phoneNumber")
    @Mapping(target = "courierEmail", source = "courier.email")
    @Mapping(target = "courierPhoneNumber", source = "courier.phoneNumber")
    @Mapping(target = "products", source = "productOnlineOrderAssociations")
    OnlineOrderDetailsDto mapToOnlineOrderDetailsDto(OnlineOrder onlineOrder);

    @Mapping(target = "id", source = "id.productId")
    @Mapping(target = "name", source = "product.name")
    OnlineOrderProductDetailsDto mapToOnlineOrderProductDetailsDto(ProductOnlineOrderAssociation productOnlineOrderAssociation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courier", ignore = true)
    @Mapping(target = "productOnlineOrderAssociations", ignore = true)
    @Mapping(target = "onlineOrderStatus", constant = "IN_PREPARATION")
    OnlineOrder mapToOnlineOrder(OnlineOrderCreationDto onlineOrderCreationDto, Double price, Instant creationTime, Client client);
}
