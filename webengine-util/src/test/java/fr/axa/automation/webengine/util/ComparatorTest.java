package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparatorTest {

    @Test
    public void testCompareBothNumericIntegers() {
        assertTrue(Comparator.compare("123", "123", false));
        assertFalse(Comparator.compare("123", "124", false));
    }

    @Test
    public void testCompareBothNumericDoubles() {
        assertTrue(Comparator.compare("123.45", "123.45", false));
        assertFalse(Comparator.compare("123.45", "123.46", false));
    }

    @Test
    public void testCompareMixedNumeric() {
        assertTrue(Comparator.compare("123", "123.0", false));
        assertFalse(Comparator.compare("123", "123.1", false));
    }

    @Test
    public void testCompareNonNumeric() {
        assertTrue(Comparator.compare("abc", "abc", false));
        assertFalse(Comparator.compare("abc", "abcd", false));
    }

    @Test
    public void testCompareWithContains() {
        assertTrue(Comparator.compare("abcdef", "abc****", true));
    }

    @Test
    public void testCompareWithContainsFalse() {
        assertFalse(Comparator.compare("abcdef", "abc****", false));
    }

    @Test
    public void testCompareNonNumericWithContains() {
        assertTrue(Comparator.compare("abcdef", "abcdef", true));
        assertFalse(Comparator.compare("abcdef", "abcd", true));
    }
}