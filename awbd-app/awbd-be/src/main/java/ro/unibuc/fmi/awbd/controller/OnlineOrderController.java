package ro.unibuc.fmi.awbd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.awbd.common.mapper.PaginationMapper;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.LogUtils;
import ro.unibuc.fmi.awbd.controller.api.OnlineOrderApi;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderCreationDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrderDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.OnlineOrdersPageDto;
import ro.unibuc.fmi.awbd.service.onlineorder.OnlineOrderService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OnlineOrderController implements OnlineOrderApi {
    private static final Integer PAGE_SIZE_LIMIT = 50;

    private final OnlineOrderService onlineOrderService;
    private final PaginationMapper paginationMapper;

    @Override
    public OnlineOrdersPageDto getOnlineOrdersPage(
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "1") Long offset
    ) {
        log.info(LogUtils.GET_ONLINE_ORDERS_PAGE_REQUEST);

        val paginationRequest = paginationMapper.mapToPaginationRequest(
                offset,
                Math.min(limit, PAGE_SIZE_LIMIT),
                "-creation_time"
        );

        return onlineOrderService.getOnlineOrdersPage(PageRequest.of(null, paginationRequest));
    }

    @Override
    public OnlineOrderDetailsDto getOnlineOrderDetails(
            @PathVariable("onlineOrderId") Long onlineOrderId
    ) {
        log.info(LogUtils.GET_ONLINE_ORDER_DETAILS_REQUEST);
        return onlineOrderService.getOnlineOrderDetails(onlineOrderId);
    }

    @Override
    public void createOnlineOrder(
            @RequestBody OnlineOrderCreationDto onlineOrderCreationDto
    ) {
        log.info(LogUtils.CREATE_ONLINE_ORDER_REQUEST);
        onlineOrderService.createOnlineOrder(onlineOrderCreationDto);
    }

    @Override
    public void updateOnlineOrderStatus(
            @PathVariable("onlineOrderId") Long onlineOrderId
    ) {
        log.info(LogUtils.UPDATE_ONLINE_ORDER_STATUS_REQUEST);
        onlineOrderService.updateOnlineOrderStatus(onlineOrderId);
    }
}
