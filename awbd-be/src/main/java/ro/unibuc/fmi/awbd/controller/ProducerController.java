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
import ro.unibuc.fmi.awbd.controller.api.ProducerApi;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.service.producer.ProducerService;
import ro.unibuc.fmi.awbd.service.producer.mapper.ProducerMapper;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerFilter;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerSortableColumn;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProducerController implements ProducerApi {
    private static final Integer PAGE_SIZE_LIMIT = 50;
    private static final String DEFAULT_SORT = "+name";

    private final ProducerMapper producerMapper;
    private final PaginationMapper paginationMapper;
    private final ProducerService producerService;

    @Override
    public ProducersPageDto getProducersPage(
            @RequestParam(value = "sort", required = false, defaultValue = "+name") String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "1") Long offset,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "address", required = false) String address
    ) {
        log.info(LogUtils.GET_PRODUCERS_PAGE_REQUEST);

        val producerFilter = Optional.ofNullable(producerMapper.mapToProducerFilter(
           name,
           address
        )).orElse(new ProducerFilter());

        val paginationRequest = paginationMapper.mapToPaginationRequest(
                offset,
                Math.min(limit, PAGE_SIZE_LIMIT),
                parseSort(sort)
        );

        return producerService.getProducersPage(PageRequest.of(producerFilter, paginationRequest));
    }

    @Override
    public List<ProducerSearchDetailsDto> getProducers() {
        log.info(LogUtils.GET_PRODUCERS_REQUEST);
        return producerService.getProducers();
    }

    @Override
    public ProducerDetailsDto getProducerDetails(
            @PathVariable("producerId") Long producerId
    ) {
        log.info(LogUtils.GET_PRODUCER_DETAILS_REQUEST);
        return producerService.getProducerDetails(producerId);
    }

    @Override
    public void createProducer(
            @RequestBody ProducerCreationDto producerCreationDto
    ) {
        log.info(LogUtils.CREATE_PRODUCER_REQUEST);
        producerService.createProducer(producerCreationDto);
    }

    @Override
    public void updateProducer(
            @PathVariable("producerId") Long producerId,
            @RequestBody ProducerUpdateDto producerUpdateDto
    ) {
        log.info(LogUtils.UPDATE_PRODUCER_REQUEST);
        producerService.updateProducer(producerId, producerUpdateDto);
    }

    @Override
    public void deleteProducer(
            @PathVariable("producerId") Long producerId
    ) {
        log.info(LogUtils.DELETE_PRODUCER_REQUEST);
        producerService.deleteProducer(producerId);
    }

    private String parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return DEFAULT_SORT;
        }

        sort = sort.toLowerCase().strip();

        if (!sort.startsWith("+") && !sort.startsWith("-")) {
            sort = "+" + sort;
        }

        if (!ProducerSortableColumn.isValidSortableColumn(sort.substring(1))) {
            return DEFAULT_SORT;
        }

        return sort;
    }
}
