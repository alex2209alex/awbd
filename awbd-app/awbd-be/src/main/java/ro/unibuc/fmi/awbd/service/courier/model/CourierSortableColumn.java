package ro.unibuc.fmi.awbd.service.courier.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;

@Getter
@RequiredArgsConstructor
public enum CourierSortableColumn {
    EMAIL("email"),
    NAME("name"),
    PHONE_NUMBER("phone_number"),
    SALARY("salary");

    private final String columnName;

    public static boolean isValidSortableColumn(String sortableColumn) {
        for (val column : CourierSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(sortableColumn)) {
                return true;
            }
        }

        return false;
    }

    public static CourierSortableColumn fromValue(String value) {
        for (val column : CourierSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(value)) {
                return column;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessageUtils.INVALID_SORTABLE_COLUMN, "CourierSortableColumn", value));
    }
}
