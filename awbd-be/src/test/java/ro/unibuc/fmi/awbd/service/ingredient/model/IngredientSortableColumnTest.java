package ro.unibuc.fmi.awbd.service.ingredient.model;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class IngredientSortableColumnTest {
    record ValueAndSortableColumn(String value, IngredientSortableColumn sortableColumn) {
    }

    @Test
    void testIsValidSortableColumn() {
        Assertions.assertFalse(IngredientSortableColumn.isValidSortableColumn("invalid"));
        Assertions.assertTrue(IngredientSortableColumn.isValidSortableColumn("name"));
    }

    @Test
    void givenInvalidIngredientSortableColumnValue_whenFromValue_thenIllegalArgumentException() {
        val exc = Assertions.assertThrows(IllegalArgumentException.class, () -> IngredientSortableColumn.fromValue("invalid"));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Invalid IngredientSortableColumn: invalid", exc.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideValueAndSortableColumn")
    void givenValidIngredientSortableColumnValue_whenFromValue_thenReturnIngredientSortableColumn(ValueAndSortableColumn valueAndSortableColumn) {
        val column = IngredientSortableColumn.fromValue(valueAndSortableColumn.value);
        Assertions.assertEquals(valueAndSortableColumn.sortableColumn, column);
    }

    private static Stream<ValueAndSortableColumn> provideValueAndSortableColumn() {
        Assertions.assertEquals(4, IngredientSortableColumn.values().length);

        return Stream.of(
                new ValueAndSortableColumn("name", IngredientSortableColumn.NAME),
                new ValueAndSortableColumn("price", IngredientSortableColumn.PRICE),
                new ValueAndSortableColumn("calories", IngredientSortableColumn.CALORIES),
                new ValueAndSortableColumn("producer", IngredientSortableColumn.PRODUCER)
        );
    }
}
