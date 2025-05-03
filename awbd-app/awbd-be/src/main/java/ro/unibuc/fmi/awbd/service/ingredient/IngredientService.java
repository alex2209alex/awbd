package ro.unibuc.fmi.awbd.service.ingredient;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.ingredient.repository.IngredientRepository;
import ro.unibuc.fmi.awbd.domain.ingredient.repository.IngredientSearchRepository;
import ro.unibuc.fmi.awbd.domain.producer.repository.ProducerRepository;
import ro.unibuc.fmi.awbd.service.ingredient.mapper.IngredientMapper;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientFilter;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientPageElementDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientSearchRepository ingredientSearchRepository;
    private final IngredientRepository ingredientRepository;
    private final ProducerRepository producerRepository;
    private final IngredientMapper ingredientMapper;

    // TODO validate user is restaurant admin for all functions

    @Transactional(readOnly = true)
    public IngredientsPageDto getIngredientsPage(PageRequest<IngredientFilter> pageRequest) {
        Page<IngredientPageElementDetails> page = ingredientSearchRepository.getIngredientsPage(pageRequest);
        return ingredientMapper.mapToIngredientsPageDto(page);
    }

    @Transactional(readOnly = true)
    public List<IngredientSearchDetailsDto> getIngredients() {
        val ingredients = ingredientRepository.findAllByOrderByName();
        return ingredientMapper.mapToIngredientSearchDetailsDtos(ingredients);
    }

    @Transactional(readOnly = true)
    public IngredientDetailsDto getIngredientDetails(Long ingredientId) {
        val ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.INGREDIENT_NOT_FOUND, ingredientId))
        );
        return ingredientMapper.mapToIngredientDetailsDto(ingredient);
    }

    @Transactional
    public void createIngredient(IngredientCreationDto ingredientCreationDto) {
        val producer = producerRepository.findById(ingredientCreationDto.getProducerId()).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCER_NOT_FOUND, ingredientCreationDto.getProducerId()))
        );
        val ingredient = ingredientMapper.mapToIngredient(ingredientCreationDto, producer);
        ingredientRepository.save(ingredient);
    }

    @Transactional
    public void updateIngredient(Long ingredientId, IngredientUpdateDto ingredientUpdateDto) {
        val producer = producerRepository.findById(ingredientUpdateDto.getProducerId()).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCER_NOT_FOUND, ingredientUpdateDto.getProducerId()))
        );
        val ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.INGREDIENT_NOT_FOUND, ingredientId))
        );
        ingredientMapper.mergeToIngredient(ingredient, ingredientUpdateDto, producer);
    }

    @Transactional
    public void deleteIngredient(Long ingredientId) {
        val ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.INGREDIENT_NOT_FOUND, ingredientId))
        );
        if (!ingredient.getIngredientProductAssociations().isEmpty()) {
            throw new ForbiddenException(String.format(ErrorMessageUtils.INGREDIENT_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED, ingredientId));
        }
        ingredientRepository.delete(ingredient);
    }
}
