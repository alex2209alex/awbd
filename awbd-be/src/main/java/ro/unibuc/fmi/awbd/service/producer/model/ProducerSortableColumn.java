package ro.unibuc.fmi.awbd.service.producer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;

@Getter
@RequiredArgsConstructor
public enum ProducerSortableColumn {
    NAME("name"),
    ADDRESS("address");

    private final String columnName;

    public static boolean isValidSortableColumn(String sort) {
        for (val column : ProducerSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(sort)) {
                return true;
            }
        }

        return false;
    }

    public static ProducerSortableColumn fromValue(String value) {
        for (val column : ProducerSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(value)) {
                return column;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessageUtils.INVALID_SORTABLE_COLUMN, "ProducerSortableColumn", value));
    }
}
