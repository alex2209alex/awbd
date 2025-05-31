package ro.unibuc.fmi.awbd.service.courier.model;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class CourierSortableColumnTest {
    record ValueAndSortableColumn(String value, CourierSortableColumn sortableColumn) {
    }

    @Test
    void testIsValidSortableColumn() {
        Assertions.assertFalse(CourierSortableColumn.isValidSortableColumn("invalid"));
        Assertions.assertTrue(CourierSortableColumn.isValidSortableColumn("email"));
    }

    @Test
    void givenInvalidCourierSortableColumnValue_whenFromValue_thenIllegalArgumentException() {
        val exc = Assertions.assertThrows(IllegalArgumentException.class, () -> CourierSortableColumn.fromValue("invalid"));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Invalid CourierSortableColumn: invalid", exc.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideValueAndSortableColumn")
    void givenValidCourierSortableColumnValue_whenFromValue_thenReturnCourierSortableColumn(ValueAndSortableColumn valueAndSortableColumn) {
        val column = CourierSortableColumn.fromValue(valueAndSortableColumn.value);
        Assertions.assertEquals(valueAndSortableColumn.sortableColumn, column);
    }

    private static Stream<ValueAndSortableColumn> provideValueAndSortableColumn() {
        Assertions.assertEquals(4, CourierSortableColumn.values().length);

        return Stream.of(
                new ValueAndSortableColumn("email", CourierSortableColumn.EMAIL),
                new ValueAndSortableColumn("name", CourierSortableColumn.NAME),
                new ValueAndSortableColumn("phone_number", CourierSortableColumn.PHONE_NUMBER),
                new ValueAndSortableColumn("salary", CourierSortableColumn.SALARY)
        );
    }
}
