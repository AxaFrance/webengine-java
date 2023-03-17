package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class RegexUtilTest {

    private final static String VALUE_REFERENCE_REGEX = "([<]{3}.*?[>]{3})?";
    private static final ILoggerService loggerService = new LoggerService();

    @Test
    void testMatchWithOnlyDataReference() {
        Set<String> list = RegexUtil.match(VALUE_REFERENCE_REGEX,"<<<num_contrat>>>TEST1<<<num_client>>>TEST2");
        Assertions.assertTrue(list.contains("<<<num_contrat>>>"));
        Assertions.assertTrue(list.contains("<<<num_client>>>"));
    }
}