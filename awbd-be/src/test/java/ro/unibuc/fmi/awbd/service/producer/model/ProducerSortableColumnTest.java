package ro.unibuc.fmi.awbd.service.producer.model;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ProducerSortableColumnTest {
    record ValueAndSortableColumn(String value, ProducerSortableColumn sortableColumn) {
    }

    @Test
    void testIsValidSortableColumn() {
        Assertions.assertFalse(ProducerSortableColumn.isValidSortableColumn("invalid"));
        Assertions.assertTrue(ProducerSortableColumn.isValidSortableColumn("name"));
    }

    @Test
    void givenInvalidProducerSortableColumnValue_whenFromValue_thenIllegalArgumentException() {
        val exc = Assertions.assertThrows(IllegalArgumentException.class, () -> ProducerSortableColumn.fromValue("invalid"));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Invalid ProducerSortableColumn: invalid", exc.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideValueAndSortableColumn")
    void givenValidProducerSortableColumnValue_whenFromValue_thenReturnProducerSortableColumn(ValueAndSortableColumn valueAndSortableColumn) {
        val column = ProducerSortableColumn.fromValue(valueAndSortableColumn.value);
        Assertions.assertEquals(valueAndSortableColumn.sortableColumn, column);
    }

    private static Stream<ValueAndSortableColumn> provideValueAndSortableColumn() {
        Assertions.assertEquals(2, ProducerSortableColumn.values().length);

        return Stream.of(
                new ValueAndSortableColumn("name", ProducerSortableColumn.NAME),
                new ValueAndSortableColumn("address", ProducerSortableColumn.ADDRESS)
        );
    }
}
