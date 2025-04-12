package ro.unibuc.fmi.awbd.common.utils;

public class ErrorMessageUtils {

    private ErrorMessageUtils() {
    }

    public static final String PRODUCER_NOT_FOUND = "Producer with ID %s not found";
    public static final String PRODUCER_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED = "Producer with ID %s has dependencies and cannot be deleted";

    public static final String INGREDIENT_NOT_FOUND = "Ingredient with ID %s not found";
    public static final String INGREDIENT_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED = "Ingredient with ID %s has dependencies and cannot be deleted";

    public static final String PRODUCT_NOT_FOUND = "Product with ID %s not found";
    public static final String DUPLICATE_INGREDIENTS_PRESENT = "Duplicate ingredients present";
    public static final String INGREDIENTS_NOT_FOUND = "Ingredients not found";
    public static final String PRODUCT_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED = "Product with ID %s has dependencies and cannot be deleted";

    public static final String INVALID_SORTABLE_COLUMN = "Invalid %s: %s";
}
