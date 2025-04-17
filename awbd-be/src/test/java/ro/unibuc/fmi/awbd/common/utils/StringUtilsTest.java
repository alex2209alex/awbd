package ro.unibuc.fmi.awbd.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {
    @Test
    void testIsNullOrBlank() {
        Assertions.assertTrue(StringUtils.isNullOrBlank(null));
        Assertions.assertTrue(StringUtils.isNullOrBlank("    "));
        Assertions.assertFalse(StringUtils.isNullOrBlank("a"));

    }
}
