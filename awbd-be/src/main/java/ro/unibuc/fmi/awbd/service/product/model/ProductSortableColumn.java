package ro.unibuc.fmi.awbd.service.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;

@Getter
@RequiredArgsConstructor
public enum ProductSortableColumn {
    NAME("name"),
    PRICE("price"),
    DESCRIPTION("description"),
    CALORIES("calories");

    private final String columnName;

    public static boolean isValidSortableColumn(String sortableColumn) {
        for (val column : ProductSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(sortableColumn)) {
                return true;
            }
        }

        return false;
    }

    public static ProductSortableColumn fromValue(String value) {
        for (val column : ProductSortableColumn.values()) {
            if (column.name().equalsIgnoreCase(value)) {
                return column;
            }
        }

        throw new IllegalArgumentException(String.format(ErrorMessageUtils.INVALID_SORTABLE_COLUMN, "ProductSortableColumn", value));
    }
}
