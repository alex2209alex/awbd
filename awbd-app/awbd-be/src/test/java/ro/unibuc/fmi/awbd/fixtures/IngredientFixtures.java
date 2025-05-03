package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.controller.models.IngredientCreationDto;
import ro.unibuc.fmi.awbd.controller.models.IngredientUpdateDto;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientPageElementDetails;

import java.util.List;

public class IngredientFixtures {
    private IngredientFixtures() {
    }

    public static Page<IngredientPageElementDetails> getPageOfIngredientPageElementDetailsFixture() {
        return Page.<IngredientPageElementDetails>builder()
                .items(List.of(getIngredientPageElementDetailsFixture()))
                .paginationInformation(PaginationInformationFixtures.getPaginationInformationFixture())
                .build();
    }

    public static Ingredient getIngredientFixture() {
        val ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("name");
        ingredient.setPrice(19.99);
        ingredient.setCalories(100.);
        ingredient.setProducer(ProducerFixtures.getProducerFixture());
        return ingredient;
    }

    public static IngredientCreationDto getIngredientCreationDtoFixture() {
        return new IngredientCreationDto("name", 19.99, 100., 1L);
    }

    public static IngredientUpdateDto getIngredientUpdateDtoFixture() {
        return new IngredientUpdateDto("name", 19.99, 100., 1L);
    }

    private static IngredientPageElementDetails getIngredientPageElementDetailsFixture() {
        return IngredientPageElementDetails.builder()
                .id(1L)
                .name("name")
                .price(19.99)
                .calories(100.)
                .producer("producer")
                .build();
    }
}
