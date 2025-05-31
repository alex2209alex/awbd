package ro.unibuc.fmi.awbd.it;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.domain.ingredient.repository.IngredientSearchRepository;
import ro.unibuc.fmi.awbd.service.ingredient.model.IngredientFilter;

@IntegrationTest
@Sql(scripts = {"/db/ingredient_search_repository_test.sql"})
@Transactional
class IngredientSearchRepositoryIT {
    @Autowired
    private IngredientSearchRepository ingredientSearchRepository;

    @Test
    void givenNoSearchParameters_whenGetIngredientsPage_thenReturnIngredientsPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("-producer")
                .build();
        val filter = new IngredientFilter();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        var ingredientsPage = ingredientSearchRepository.getIngredientsPage(pageRequest);

        Assertions.assertNotNull(ingredientsPage);
        Assertions.assertNotNull(ingredientsPage.getItems());
        Assertions.assertEquals(2, ingredientsPage.getItems().size());
        Assertions.assertEquals(2, ingredientsPage.getItems().getFirst().getId());
        Assertions.assertEquals("Ingredient 2", ingredientsPage.getItems().getFirst().getName());
        Assertions.assertEquals(20., ingredientsPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(20., ingredientsPage.getItems().getFirst().getCalories());
        Assertions.assertEquals("Producer 2", ingredientsPage.getItems().getFirst().getProducer());
        Assertions.assertEquals(1, ingredientsPage.getItems().get(1).getId());
        Assertions.assertEquals("Ingredient 1", ingredientsPage.getItems().get(1).getName());
        Assertions.assertEquals(10., ingredientsPage.getItems().get(1).getPrice());
        Assertions.assertEquals(10., ingredientsPage.getItems().get(1).getCalories());
        Assertions.assertEquals("Producer 1", ingredientsPage.getItems().get(1).getProducer());

        paginationRequest.setSort("+price");

        ingredientsPage = ingredientSearchRepository.getIngredientsPage(pageRequest);

        Assertions.assertNotNull(ingredientsPage);
        Assertions.assertNotNull(ingredientsPage.getItems());
        Assertions.assertEquals(2, ingredientsPage.getItems().size());
        Assertions.assertEquals(1, ingredientsPage.getItems().getFirst().getId());
        Assertions.assertEquals(2, ingredientsPage.getItems().get(1).getId());

        paginationRequest.setSort("-calories");

        ingredientsPage = ingredientSearchRepository.getIngredientsPage(pageRequest);

        Assertions.assertNotNull(ingredientsPage);
        Assertions.assertNotNull(ingredientsPage.getItems());
        Assertions.assertEquals(2, ingredientsPage.getItems().size());
        Assertions.assertEquals(2, ingredientsPage.getItems().getFirst().getId());
        Assertions.assertEquals(1, ingredientsPage.getItems().get(1).getId());
    }

    @Test
    void givenSearchParameters_whenGetIngredientsPage_thenReturnIngredientsPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("+name")
                .build();
        val filter = IngredientFilter.builder()
                .name("Ingredient 1")
                .producer("producer")
                .build();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        val ingredientsPage = ingredientSearchRepository.getIngredientsPage(pageRequest);

        Assertions.assertNotNull(ingredientsPage);
        Assertions.assertNotNull(ingredientsPage.getItems());
        Assertions.assertEquals(1, ingredientsPage.getItems().size());
        Assertions.assertEquals(1, ingredientsPage.getItems().getFirst().getId());
        Assertions.assertEquals("Ingredient 1", ingredientsPage.getItems().getFirst().getName());
        Assertions.assertEquals(10., ingredientsPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(10., ingredientsPage.getItems().getFirst().getCalories());
        Assertions.assertEquals("Producer 1", ingredientsPage.getItems().getFirst().getProducer());
    }
}
