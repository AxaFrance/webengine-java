package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RegexUtilTest {

    private final static String VALUE_REFERENCE_REGEX = "([<]{3}.*?[>]{3})*";
//    private final static String VALUE_REFERENCE_REGEX_2 = "([<]{3}.*?[>]{3}){1}(\\+1)?";
private final static String VALUE_REFERENCE_REGEX_2 = "(?<=[<]{3}).*?(?=[>]{3})\\+1";
    private static final ILoggerService loggerService = new LoggerService();

    @Test
    void testMatchWithOnlyDataReference() {
        List<String> list = RegexUtil.match("\\d+"," toto dont le num client est <<<num_client>>>  à fait quelque chose le <<<TODAY+1>>>");
        Assertions.assertTrue(list.contains("<<<num_contrat>>>"));
        Assertions.assertTrue(list.contains("<<<num_client>>>"));
    }
}