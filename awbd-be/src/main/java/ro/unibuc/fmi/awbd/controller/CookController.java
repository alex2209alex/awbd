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
import ro.unibuc.fmi.awbd.controller.api.CookApi;
import ro.unibuc.fmi.awbd.controller.models.CookCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CookDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CookUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.CooksPageDto;
import ro.unibuc.fmi.awbd.service.cook.CookService;
import ro.unibuc.fmi.awbd.service.cook.mapper.CookMapper;
import ro.unibuc.fmi.awbd.service.cook.model.CookFilter;
import ro.unibuc.fmi.awbd.service.cook.model.CookSortableColumn;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CookController implements CookApi {
    private static final Integer PAGE_SIZE_LIMIT = 50;
    private static final String DEFAULT_SORT = "+email";

    private final CookMapper cookMapper;
    private final PaginationMapper paginationMapper;
    private final CookService cookService;

    @Override
    public CooksPageDto getCooksPage(
            @RequestParam(value = "sort", required = false, defaultValue = "+email") String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "1") Long offset,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "name", required = false) String name
    ) {
        log.info(LogUtils.GET_COOKS_PAGE_REQUEST);

        val cookFilter = Optional.ofNullable(cookMapper.mapToCookFilter(
                email,
                name
        )).orElse(new CookFilter());

        val paginationRequest = paginationMapper.mapToPaginationRequest(
                offset,
                Math.min(limit, PAGE_SIZE_LIMIT),
                parseSort(sort)
        );

        return cookService.getCooksPage(PageRequest.of(cookFilter, paginationRequest));
    }

    @Override
    public CookDetailsDto getCookDetails(
            @PathVariable("cookId") Long cookId
    ) {
        log.info(LogUtils.GET_COOK_DETAILS_REQUEST);
        return cookService.getCookDetails(cookId);
    }

    @Override
    public void createCook(
            @RequestBody CookCreationDto cookCreationDto
    ) {
        log.info(LogUtils.CREATE_COOK_REQUEST);
        cookService.createCook(cookCreationDto);
    }

    @Override
    public void updateCook(
            @PathVariable("cookId") Long cookId,
            @RequestBody CookUpdateDto cookUpdateDto
    ) {
        log.info(LogUtils.UPDATE_COOK_REQUEST);
        cookService.updateCook(cookId, cookUpdateDto);
    }

    @Override
    public void deleteCook(
            @PathVariable("cookId") Long cookId
    ) {
        log.info(LogUtils.DELETE_COOK_REQUEST);
        cookService.deleteCook(cookId);
    }

    private String parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return DEFAULT_SORT;
        }

        sort = sort.toLowerCase().strip();

        if (!sort.startsWith("+") && !sort.startsWith("-")) {
            sort = "+" + sort;
        }

        if (!CookSortableColumn.isValidSortableColumn(sort.substring(1))) {
            return DEFAULT_SORT;
        }

        return sort;
    }
}
