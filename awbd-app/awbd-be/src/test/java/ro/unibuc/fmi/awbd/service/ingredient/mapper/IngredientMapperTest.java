package ro.unibuc.fmi.awbd.service.ingredient.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.fixtures.IngredientFixtures;
import ro.unibuc.fmi.awbd.fixtures.ProducerFixtures;

import java.util.List;

@SpringBootTest(classes = {IngredientMapperImpl.class})
class IngredientMapperTest {
    @Autowired
    private IngredientMapper ingredientMapper;

    @Test
    void testMapToIngredientFilter() {
        Assertions.assertNull(ingredientMapper.mapToIngredientFilter(null, null));

        val name = "name";
        val producer = "producer";

        var ingredientFilter = ingredientMapper.mapToIngredientFilter(name, producer);

        Assertions.assertNotNull(ingredientFilter);
        Assertions.assertEquals(name, ingredientFilter.getName());
        Assertions.assertEquals(producer, ingredientFilter.getProducer());

        ingredientFilter = ingredientMapper.mapToIngredientFilter(null, producer);

        Assertions.assertNotNull(ingredientFilter);
        Assertions.assertNull(ingredientFilter.getName());
        Assertions.assertEquals(producer, ingredientFilter.getProducer());

        ingredientFilter = ingredientMapper.mapToIngredientFilter(name, null);

        Assertions.assertNotNull(ingredientFilter);
        Assertions.assertEquals(name, ingredientFilter.getName());
        Assertions.assertNull(ingredientFilter.getProducer());
    }

    @Test
    void testMapToIngredientsPageDto() {
        Assertions.assertNull(ingredientMapper.mapToIngredientsPageDto(null));

        val page = IngredientFixtures.getPageOfIngredientPageElementDetailsFixture();

        val ingredientsPageDto = ingredientMapper.mapToIngredientsPageDto(page);

        Assertions.assertNotNull(ingredientsPageDto);

        Assertions.assertNotNull(ingredientsPageDto.getPagination());
        Assertions.assertEquals(ingredientsPageDto.getPagination().getPage(), page.getPaginationInformation().getPage());
        Assertions.assertEquals(ingredientsPageDto.getPagination().getPageSize(), page.getPaginationInformation().getPageSize());
        Assertions.assertEquals(ingredientsPageDto.getPagination().getPagesTotal(), page.getPaginationInformation().getPagesTotal());
        Assertions.assertEquals(ingredientsPageDto.getPagination().getHasNextPage(), page.getPaginationInformation().isHasNextPage());
        Assertions.assertEquals(ingredientsPageDto.getPagination().getItemsTotal(), page.getPaginationInformation().getItemsTotal());
        Assertions.assertEquals(ingredientsPageDto.getPagination().getSort(), page.getPaginationInformation().getSort());

        Assertions.assertNotNull(ingredientsPageDto.getItems());
        Assertions.assertEquals(page.getItems().size(), ingredientsPageDto.getItems().size());
        for (int i = 0; i < ingredientsPageDto.getItems().size(); i++) {
            Assertions.assertEquals(page.getItems().get(i).getId(), ingredientsPageDto.getItems().get(i).getId());
            Assertions.assertEquals(page.getItems().get(i).getName(), ingredientsPageDto.getItems().get(i).getName());
            Assertions.assertEquals(page.getItems().get(i).getPrice(), ingredientsPageDto.getItems().get(i).getPrice());
            Assertions.assertEquals(page.getItems().get(i).getCalories(), ingredientsPageDto.getItems().get(i).getCalories());
            Assertions.assertEquals(page.getItems().get(i).getProducer(), ingredientsPageDto.getItems().get(i).getProducer());
        }
    }

    @Test
    void testMapToIngredientSearchDetailsDtos() {
        Assertions.assertNull(ingredientMapper.mapToIngredientSearchDetailsDtos(null));

        val ingredient = IngredientFixtures.getIngredientFixture();

        val ingredientSearchDetailsDtos = ingredientMapper.mapToIngredientSearchDetailsDtos(List.of(ingredient));

        Assertions.assertNotNull(ingredientSearchDetailsDtos);
        Assertions.assertEquals(1, ingredientSearchDetailsDtos.size());
        Assertions.assertEquals(ingredient.getId(), ingredientSearchDetailsDtos.getFirst().getId());
        Assertions.assertEquals(ingredient.getName(), ingredientSearchDetailsDtos.getFirst().getName());
    }

    @Test
    void testMapToIngredientDetailsDto() {
        Assertions.assertNull(ingredientMapper.mapToIngredientDetailsDto(null));

        val ingredient = IngredientFixtures.getIngredientFixture();

        val ingredientDetailsDto = ingredientMapper.mapToIngredientDetailsDto(ingredient);

        Assertions.assertNotNull(ingredientDetailsDto);
        Assertions.assertEquals(ingredient.getId(), ingredientDetailsDto.getId());
        Assertions.assertEquals(ingredient.getName(), ingredientDetailsDto.getName());
        Assertions.assertEquals(ingredient.getPrice(), ingredientDetailsDto.getPrice());
        Assertions.assertEquals(ingredient.getCalories(), ingredientDetailsDto.getCalories());
        Assertions.assertEquals(ingredient.getProducer().getId(), ingredientDetailsDto.getProducerId());
        Assertions.assertEquals(ingredient.getProducer().getName(), ingredientDetailsDto.getProducerName());
    }

    @Test
    void testMapToIngredient() {
        Assertions.assertNull(ingredientMapper.mapToIngredient(null, null));

        val ingredientCreationDto = IngredientFixtures.getIngredientCreationDtoFixture();
        val producer = ProducerFixtures.getProducerFixture();

        val ingredient = ingredientMapper.mapToIngredient(ingredientCreationDto, producer);

        Assertions.assertNotNull(ingredient);
        Assertions.assertEquals(ingredientCreationDto.getName(), ingredient.getName());
        Assertions.assertEquals(ingredientCreationDto.getPrice(), ingredient.getPrice());
        Assertions.assertEquals(ingredientCreationDto.getCalories(), ingredient.getCalories());
        Assertions.assertEquals(producer, ingredient.getProducer());
    }

    @Test
    void testMergeToIngredient() {
        val ingredient = IngredientFixtures.getIngredientFixture();

        Assertions.assertDoesNotThrow(() -> ingredientMapper.mergeToIngredient(ingredient, null, null));

        ingredient.setName("old");
        ingredient.setPrice(-19.99);
        ingredient.setCalories(-100.);
        ingredient.setProducer(ProducerFixtures.getProducerFixture());
        val ingredientUpdateDto = IngredientFixtures.getIngredientUpdateDtoFixture();
        val producer = ProducerFixtures.getProducerFixture();

        ingredientMapper.mergeToIngredient(ingredient, ingredientUpdateDto, producer);

        Assertions.assertEquals(ingredientUpdateDto.getName(), ingredient.getName());
        Assertions.assertEquals(ingredientUpdateDto.getPrice(), ingredient.getPrice());
        Assertions.assertEquals(ingredientUpdateDto.getCalories(), ingredient.getCalories());
        Assertions.assertEquals(producer, ingredient.getProducer());
    }
}
