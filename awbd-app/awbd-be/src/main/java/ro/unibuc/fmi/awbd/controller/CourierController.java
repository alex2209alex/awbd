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
import ro.unibuc.fmi.awbd.controller.api.CourierApi;
import ro.unibuc.fmi.awbd.controller.models.CourierCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CourierDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CourierUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.CouriersPageDto;
import ro.unibuc.fmi.awbd.service.courier.CourierService;
import ro.unibuc.fmi.awbd.service.courier.mapper.CourierMapper;
import ro.unibuc.fmi.awbd.service.courier.model.CourierFilter;
import ro.unibuc.fmi.awbd.service.courier.model.CourierSortableColumn;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CourierController implements CourierApi {
    private static final Integer PAGE_SIZE_LIMIT = 50;
    private static final String DEFAULT_SORT = "+email";

    private final CourierMapper courierMapper;
    private final PaginationMapper paginationMapper;
    private final CourierService courierService;

    @Override
    public CouriersPageDto getCouriersPage(
            @RequestParam(value = "sort", required = false, defaultValue = "+email") String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "1") Long offset,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone_number", required = false) String phoneNumber
    ) {
        log.info(LogUtils.GET_COURIERS_PAGE_REQUEST);

        val courierFilter = Optional.ofNullable(courierMapper.mapToCourierFilter(
                email,
                name,
                phoneNumber
        )).orElse(new CourierFilter());

        val paginationRequest = paginationMapper.mapToPaginationRequest(
                offset,
                Math.min(limit, PAGE_SIZE_LIMIT),
                parseSort(sort)
        );

        return courierService.getCouriersPage(PageRequest.of(courierFilter, paginationRequest));
    }

    @Override
    public CourierDetailsDto getCourierDetails(
            @PathVariable("courierId") Long courierId
    ) {
        log.info(LogUtils.GET_COURIER_DETAILS_REQUEST);
        return courierService.getCourierDetails(courierId);
    }

    @Override
    public void createCourier(
            @RequestBody CourierCreationDto courierCreationDto
    ) {
        log.info(LogUtils.CREATE_COURIER_REQUEST);
        courierService.createCourier(courierCreationDto);
    }

    @Override
    public void updateCourier(
            @PathVariable("courierId") Long courierId,
            @RequestBody CourierUpdateDto courierUpdateDto
    ) {
        log.info(LogUtils.UPDATE_COURIER_REQUEST);
        courierService.updateCourier(courierId, courierUpdateDto);
    }

    @Override
    public void deleteCourier(
            @PathVariable("courierId") Long courierId
    ) {
        log.info(LogUtils.DELETE_COURIER_REQUEST);
        courierService.deleteCourier(courierId);
    }

    private String parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return DEFAULT_SORT;
        }

        sort = sort.toLowerCase().strip();

        if (!sort.startsWith("+") && !sort.startsWith("-")) {
            sort = "+" + sort;
        }

        if (!CourierSortableColumn.isValidSortableColumn(sort.substring(1))) {
            return DEFAULT_SORT;
        }

        return sort;
    }
}
