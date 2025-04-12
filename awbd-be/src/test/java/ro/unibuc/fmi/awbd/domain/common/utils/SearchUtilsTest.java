package ro.unibuc.fmi.awbd.domain.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchUtilsTest {
    @Test
    void testEscapeSpecialChars() {
        Assertions.assertNull(SearchUtils.escapeSpecialChars(null));

        Assertions.assertEquals("\\\\%  \\\\_abc", SearchUtils.escapeSpecialChars("%  _abc"));
    }
}
