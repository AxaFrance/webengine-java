package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RegexUtilTest {

    private final static String VALUE_REFERENCE_REGEX = "([<]{3}.*?[>]{3})*";


    @Test
    void testMatchWithOnlyDataReference() {
        List<String> list = RegexUtil.match(VALUE_REFERENCE_REGEX," Mon numéroclient est le <<<num_client>>>  et mon numéro contrat est la <<<num_contrat>>>");
        Assertions.assertTrue(list.contains("<<<num_contrat>>>"));
        Assertions.assertTrue(list.contains("<<<num_client>>>"));
    }
}