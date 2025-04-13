package ro.unibuc.fmi.awbd.service.cook.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;

@Getter
@RequiredArgsConstructor
public enum CookSortableColumn {
    EMAIL("email"),
    NAME("name"),
    SALARY("salary");

    private final String columnName;

    public static boolean isValidSortableColumn(String sortableColumn) {
        for (val column : CookSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(sortableColumn)) {
                return true;
            }
        }

        return false;
    }

    public static CookSortableColumn fromValue(String value) {
        for (val column : CookSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(value)) {
                return column;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessageUtils.INVALID_SORTABLE_COLUMN, "CookSortableColumn", value));
    }
}
