package ro.unibuc.fmi.awbd.service.cook.model;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class CookSortableColumnTest {
    record ValueAndSortableColumn(String value, CookSortableColumn sortableColumn) {
    }

    @Test
    void testIsValidSortableColumn() {
        Assertions.assertFalse(CookSortableColumn.isValidSortableColumn("invalid"));
        Assertions.assertTrue(CookSortableColumn.isValidSortableColumn("email"));
    }

    @Test
    void givenInvalidCookSortableColumnValue_whenFromValue_thenIllegalArgumentException() {
        val exc = Assertions.assertThrows(IllegalArgumentException.class, () -> CookSortableColumn.fromValue("invalid"));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Invalid CookSortableColumn: invalid", exc.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideValueAndSortableColumn")
    void givenValidCookSortableColumnValue_whenFromValue_thenReturnCookSortableColumn(ValueAndSortableColumn valueAndSortableColumn) {
        val column = CookSortableColumn.fromValue(valueAndSortableColumn.value);
        Assertions.assertEquals(valueAndSortableColumn.sortableColumn, column);
    }

    private static Stream<ValueAndSortableColumn> provideValueAndSortableColumn() {
        Assertions.assertEquals(3, CookSortableColumn.values().length);

        return Stream.of(
                new ValueAndSortableColumn("email", CookSortableColumn.EMAIL),
                new ValueAndSortableColumn("name", CookSortableColumn.NAME),
                new ValueAndSortableColumn("salary", CookSortableColumn.SALARY)
        );
    }
}
