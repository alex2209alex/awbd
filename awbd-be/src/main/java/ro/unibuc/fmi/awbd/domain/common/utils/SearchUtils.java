package ro.unibuc.fmi.awbd.domain.common.utils;

import lombok.val;

import java.util.List;

public class SearchUtils {
    public static final char BACKSLASH = '\\';
    private static final List<String> SPECIAL_CHARACTERS = List.of("%", "_");

    private SearchUtils() {
    }

    public static String escapeSpecialChars(String input) {
        if (input == null) {
            return null;
        }
        for (val specialChar : SPECIAL_CHARACTERS) {
            input = input.replace(specialChar, "\\\\" + specialChar);
        }
        return input;
    }
}
