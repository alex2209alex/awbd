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
import ro.unibuc.fmi.awbd.controller.api.IngredientApi;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.service.ingredient.IngredientService;
import ro.unibuc.fmi.awbd.service.ingredient.mapper.IngredientMapper;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientFilter;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientSortableColumn;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IngredientController implements IngredientApi {
    private static final Integer PAGE_SIZE_LIMIT = 50;
    private static final String DEFAULT_SORT = "+name";

    private final IngredientMapper ingredientMapper;
    private final PaginationMapper paginationMapper;
    private final IngredientService ingredientService;

    @Override
    public IngredientsPageDto getIngredientsPage(
            @RequestParam(value = "sort", required = false, defaultValue = "+name") String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "1") Long offset,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "producer", required = false) String producer
    ) {
        log.info(LogUtils.GET_INGREDIENTS_PAGE_REQUEST);

        val ingredientFilter = Optional.ofNullable(ingredientMapper.mapToIngredientFilter(
                name,
                producer
        )).orElse(new IngredientFilter());

        val paginationRequest = paginationMapper.mapToPaginationRequest(
                offset,
                Math.min(limit, PAGE_SIZE_LIMIT),
                parseSort(sort)
        );

        return ingredientService.getIngredientsPage(PageRequest.of(ingredientFilter, paginationRequest));
    }

    @Override
    public List<IngredientSearchDetailsDto> getIngredients() {
        log.info(LogUtils.GET_INGREDIENTS_REQUEST);
        return ingredientService.getIngredients();
    }

    @Override
    public IngredientDetailsDto getIngredientDetails(
            @PathVariable("ingredientId") Long ingredientId
    ) {
        log.info(LogUtils.GET_INGREDIENT_DETAILS_REQUEST);
        return ingredientService.getIngredientDetails(ingredientId);
    }

    @Override
    public void createIngredient(
            @RequestBody IngredientCreationDto ingredientCreationDto
    ) {
        log.info(LogUtils.CREATE_INGREDIENT_REQUEST);
        ingredientService.createIngredient(ingredientCreationDto);
    }

    @Override
    public void updateIngredient(
            @PathVariable("ingredientId") Long ingredientId,
            @RequestBody IngredientUpdateDto ingredientUpdateDto
    ) {
        log.info(LogUtils.UPDATE_INGREDIENT_REQUEST);
        ingredientService.updateIngredient(ingredientId, ingredientUpdateDto);
    }

    @Override
    public void deleteIngredient(
            @PathVariable("ingredientId") Long ingredientId
    ) {
        log.info(LogUtils.DELETE_INGREDIENT_REQUEST);
        ingredientService.deleteIngredient(ingredientId);
    }

    private String parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return DEFAULT_SORT;
        }

        sort = sort.toLowerCase().strip();

        if (!sort.startsWith("+") && !sort.startsWith("-")) {
            sort = "+" + sort;
        }

        if (!IngredientSortableColumn.isValidSortableColumn(sort.substring(1))) {
            return DEFAULT_SORT;
        }

        return sort;
    }
}
