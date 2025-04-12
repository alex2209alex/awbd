package ro.unibuc.fmi.awbd.service.ingredient.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientFilter;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientPageElementDetails;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface IngredientMapper {
    IngredientFilter mapToIngredientFilter(String name, String producer);

    @Mapping(target = "pagination", source = "paginationInformation")
    IngredientsPageDto mapToIngredientsPageDto(Page<IngredientPageElementDetails> page);

    List<IngredientSearchDetailsDto> mapToIngredientSearchDetailsDtos(List<Ingredient> ingredients);

    @Mapping(target = "producerId", source = "producer.id")
    @Mapping(target = "producerName", source = "producer.name")
    IngredientDetailsDto mapToIngredientDetailsDto(Ingredient ingredient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "ingredientCreationDto.name")
    Ingredient mapToIngredient(IngredientCreationDto ingredientCreationDto, Producer producer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "ingredientUpdateDto.name")
    void mergeToIngredient(@MappingTarget Ingredient ingredient, IngredientUpdateDto ingredientUpdateDto, Producer producer);
}
