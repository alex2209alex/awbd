package ro.unibuc.fmi.awbd.service.onlineorder;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderCreationDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderProductCreationDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrdersPageDto;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrderStatus;
import ro.unibuc.fmi.awbd.domain.onlineorder.repository.OnlineOrderRepository;
import ro.unibuc.fmi.awbd.domain.onlineorder.repository.OnlineOrderSearchRepository;
import ro.unibuc.fmi.awbd.domain.product.model.ProductOnlineOrderAssociation;
import ro.unibuc.fmi.awbd.domain.product.model.ProductOnlineOrderAssociationId;
import ro.unibuc.fmi.awbd.domain.product.model.Product;
import ro.unibuc.fmi.awbd.domain.product.repository.ProductRepository;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;
import ro.unibuc.fmi.awbd.service.onlineorder.mapper.OnlineOrderMapper;
import ro.unibuc.fmi.awbd.service.onlineorder.model.OnlineOrderPageElementDetails;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineOrderService {
    private final OnlineOrderRepository onlineOrderRepository;
    private final ProductRepository productRepository;
    private final OnlineOrderSearchRepository onlineOrderSearchRepository;
    private final OnlineOrderMapper onlineOrderMapper;
    private final UserInformationService userInformationService;

    @Transactional(readOnly = true)
    public OnlineOrdersPageDto getOnlineOrdersPage(PageRequest<Object> pageRequest) {
        Page<OnlineOrderPageElementDetails> page = onlineOrderSearchRepository.getOnlineOrdersPage(pageRequest);
        return onlineOrderMapper.mapToOnlineOrdersPageDto(page);
    }

    @Transactional(readOnly = true)
    public OnlineOrderDetailsDto getOnlineOrderDetails(Long onlineOrderId) {
        val onlineOrder = onlineOrderRepository.findById(onlineOrderId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.ONLINE_ORDER_NOT_FOUND, onlineOrderId))
        );
        if ((onlineOrder.getOnlineOrderStatus() != OnlineOrderStatus.IN_PREPARATION && userInformationService.isCurrentUserCook())
                || (!onlineOrder.getClient().getId().equals(userInformationService.getCurrentUser().getId()) && userInformationService.isCurrentUserClient())
                || ((onlineOrder.getOnlineOrderStatus() == OnlineOrderStatus.ON_DELIVERY || onlineOrder.getOnlineOrderStatus() == OnlineOrderStatus.DELIVERED) && !onlineOrder.getCourier().getId().equals(userInformationService.getCurrentUser().getId()) && userInformationService.isCurrentUserCourier())
        ) {
            throw new ForbiddenException(String.format(ErrorMessageUtils.USER_CANNOT_VIEW_DETAILS_OF_ONLINE_ORDER, onlineOrderId));
        }
        return onlineOrderMapper.mapToOnlineOrderDetailsDto(onlineOrder);
    }

    @Transactional
    public void createOnlineOrder(OnlineOrderCreationDto onlineOrderCreationDto) {
        userInformationService.ensureCurrentUserIsClient();
        val productIds = onlineOrderCreationDto.getProducts()
                .stream()
                .map(OnlineOrderProductCreationDto::getId)
                .collect(Collectors.toSet());
        if (productIds.size() != onlineOrderCreationDto.getProducts().size()) {
            throw new BadRequestException(ErrorMessageUtils.DUPLICATE_PRODUCTS_PRESENT);
        }
        val products = productRepository.findAllByIdIn(productIds);
        if (products.size() != onlineOrderCreationDto.getProducts().size()) {
            throw new NotFoundException(ErrorMessageUtils.PRODUCTS_NOT_FOUND);
        }

        Map<Long, Product> productsMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        val priceBeforeLoyaltyReduction = onlineOrderCreationDto.getProducts()
                .stream()
                .map(onlineOrderProductCreationDto -> onlineOrderProductCreationDto.getQuantity() * productsMap.get(onlineOrderProductCreationDto.getId()).getPrice())
                .reduce(Double::sum)
                .orElse(0.);
        val client = (Client) userInformationService.getCurrentUser();
        val price = getPriceAfterLoyaltyReduction(priceBeforeLoyaltyReduction, client);
        addLoyaltyPoints(price, client);
        val onlineOrder = onlineOrderMapper.mapToOnlineOrder(onlineOrderCreationDto, price, Instant.now(), client);
        val persistedOnlineOrder = onlineOrderRepository.save(onlineOrder);
        persistedOnlineOrder.setProductOnlineOrderAssociations(new ArrayList<>());
        onlineOrderCreationDto.getProducts().forEach(onlineOrderProductCreationDto -> {
            val productOnlineOrderAssociationId = ProductOnlineOrderAssociationId.builder()
                    .onlineOrderId(persistedOnlineOrder.getId())
                    .productId(onlineOrderProductCreationDto.getId())
                    .build();
            val productOnlineOrderAssociation = ProductOnlineOrderAssociation.builder()
                    .id(productOnlineOrderAssociationId)
                    .quantity(onlineOrderProductCreationDto.getQuantity())
                    .build();
            persistedOnlineOrder.getProductOnlineOrderAssociations().add(productOnlineOrderAssociation);
        });
    }

    @Transactional
    public void updateOnlineOrderStatus(Long onlineOrderId) {
        val onlineOrder = onlineOrderRepository.findById(onlineOrderId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.ONLINE_ORDER_NOT_FOUND, onlineOrderId))
        );
        if (onlineOrder.getOnlineOrderStatus() == OnlineOrderStatus.IN_PREPARATION && userInformationService.isCurrentUserCook()) {
            onlineOrder.setOnlineOrderStatus(OnlineOrderStatus.READY);
        } else if (onlineOrder.getOnlineOrderStatus() == OnlineOrderStatus.READY && userInformationService.isCurrentUserCourier()) {
            onlineOrder.setOnlineOrderStatus(OnlineOrderStatus.ON_DELIVERY);
            onlineOrder.setCourier((Courier) userInformationService.getCurrentUser());
        } else if (onlineOrder.getOnlineOrderStatus() == OnlineOrderStatus.ON_DELIVERY && onlineOrder.getCourier().getId().equals(userInformationService.getCurrentUser().getId())) {
            onlineOrder.setOnlineOrderStatus(OnlineOrderStatus.DELIVERED);
        } else {
            throw new ForbiddenException(String.format(ErrorMessageUtils.USER_CANNOT_UPDATE_ONLINE_ORDER_STATUS, onlineOrderId));
        }
    }

    private Double getPriceAfterLoyaltyReduction(Double priceBeforeLoyaltyReduction, Client client) {
        if (client.getLoyaltyCard() == null) {
            return priceBeforeLoyaltyReduction;
        }

        if (client.getLoyaltyCard().getPoints() >= 20000) {
            return priceBeforeLoyaltyReduction * 0.95;
        }

        if (client.getLoyaltyCard().getPoints() >= 10000) {
            return priceBeforeLoyaltyReduction * 0.97;
        }

        if (client.getLoyaltyCard().getPoints() >= 5000) {
            return priceBeforeLoyaltyReduction * 0.99;
        }

        return priceBeforeLoyaltyReduction;
    }

    private void addLoyaltyPoints(Double price, Client client) {
        if (client.getLoyaltyCard() != null) {
            client.getLoyaltyCard().setPoints(client.getLoyaltyCard().getPoints() + Math.round(10 * price));
        }
    }
}
