package ro.unibuc.fmi.awbd.service.product.model;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ProductSortableColumnTest {
    record ValueAndSortableColumn(String value, ProductSortableColumn sortableColumn) {
    }

    @Test
    void testIsValidSortableColumn() {
        Assertions.assertFalse(ProductSortableColumn.isValidSortableColumn("invalid"));
        Assertions.assertTrue(ProductSortableColumn.isValidSortableColumn("name"));
    }

    @Test
    void givenInvalidProductSortableColumnValue_whenFromValue_thenIllegalArgumentException() {
        val exc = Assertions.assertThrows(IllegalArgumentException.class, () -> ProductSortableColumn.fromValue("invalid"));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Invalid ProductSortableColumn: invalid", exc.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideValueAndSortableColumn")
    void givenValidProductSortableColumnValue_whenFromValue_thenReturnProductSortableColumn(ValueAndSortableColumn valueAndSortableColumn) {
        val column = ProductSortableColumn.fromValue(valueAndSortableColumn.value);
        Assertions.assertEquals(valueAndSortableColumn.sortableColumn, column);
    }

    private static Stream<ValueAndSortableColumn> provideValueAndSortableColumn() {
        Assertions.assertEquals(4, ProductSortableColumn.values().length);

        return Stream.of(
                new ValueAndSortableColumn("name", ProductSortableColumn.NAME),
                new ValueAndSortableColumn("price", ProductSortableColumn.PRICE),
                new ValueAndSortableColumn("description", ProductSortableColumn.DESCRIPTION),
                new ValueAndSortableColumn("calories", ProductSortableColumn.CALORIES)
        );
    }
}
