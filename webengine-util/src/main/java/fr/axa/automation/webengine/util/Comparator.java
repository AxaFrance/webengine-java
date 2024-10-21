package fr.axa.automation.webengine.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public final class Comparator {

    public static boolean compare(String actualValue, String expectedValue, boolean contains) {
        if (areBothNumeric(actualValue, expectedValue)) {
            return compareAsNumbers(actualValue, expectedValue);
        } else {
            return StringUtil.equalsIgnoreCase(actualValue, expectedValue) ||
                    (contains && (expectedValue.endsWith("****") && StringUtil.contains(actualValue, expectedValue.split("\\*{4}")[0].trim())));
        }
    }

    public static boolean areBothNumeric(String actualValue, String expectedValue) {
        return StringUtils.isNumeric(actualValue) && StringUtils.isNumeric(expectedValue) ||
                NumberUtils.isCreatable(actualValue) && NumberUtils.isCreatable(expectedValue);
    }

    public static boolean compareAsNumbers(String actualValue, String expectedValue) {
        if (StringUtils.isNumeric(actualValue) && StringUtils.isNumeric(expectedValue)) {
            return Integer.parseInt(actualValue) == Integer.parseInt(expectedValue);
        } else {
            return Double.parseDouble(actualValue) == Double.parseDouble(expectedValue);
        }
    }
}
