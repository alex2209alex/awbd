package ro.unibuc.fmi.awbd.common.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isNullOrBlank(String string) {
        return string == null || string.isBlank();
    }
}
