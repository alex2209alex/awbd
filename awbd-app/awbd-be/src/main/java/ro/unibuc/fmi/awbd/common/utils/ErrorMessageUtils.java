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

    public static final String COURIER_NOT_FOUND = "Courier with ID %s not found";
    public static final String USER_WITH_EMAIL_ALREADY_EXISTS = "User with email %s already exists";
    public static final String COURIER_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED = "Courier with ID %s has dependencies and cannot be deleted";

    public static final String COOK_NOT_FOUND = "Cook with ID %s not found";
    public static final String DUPLICATE_PRODUCTS_PRESENT = "Duplicate products present";
    public static final String PRODUCTS_NOT_FOUND = "Products not found";

    public static final String CLIENT_NOT_FOUND = "Client with ID %s not found";

    public static final String INVALID_SORTABLE_COLUMN = "Invalid %s: %s";

    public static final String ERROR_HASHING_ALGORITHM = "Error with hashing algorithm";

    public static final String AUTHORIZATION_FAILED = "Authorization failed";
}
