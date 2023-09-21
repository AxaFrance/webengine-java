package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RegexUtilTest {

    private final static String VALUE_REFERENCE_REGEX = "([<]{3}.*?[>]{3})*";
    public static final String TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN = "([\\w-]+\\[[-\\w:;]+\\])|([\\w-]+)";;

    public static final String TEST_CASE_PATTERN = "^([^\\[]+)";

    @Test
    void testMatchTestCaseAndDataTestColumn() {
        List<String> list = RegexUtil.match(TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN,"firsttestcase[-dataColumnName:jdd-rec-auto;jdd-rec-moto];testcase2[-dataColumName:jdd-rec-moto];thirdtestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto];");
        Assertions.assertTrue(list.size()==3);
    }

    @Test
    void testMatchTestCaseAndDataTestColumn2() {
        List<String> list = RegexUtil.match(TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN,"firsttestcase;testcase2[-dataColumnName:jdd-rec-moto];thirdtestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto];");
        Assertions.assertTrue(list.size()==3);
    }

    @Test
    void testMatchTestCaseAndDataTestColumn3() {
        List<String> list = RegexUtil.match(TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN,"testcase2;thirdtestcase");
        Assertions.assertTrue(list.size()==2);
    }

    @Test
    void testMatchTestCaseAndDataTestColumn4() {
        List<String> list = RegexUtil.match(TEST_CASE_AND_DATA_TEST_COLUMN_NAME_PATTERN,"testcase2[-dataColumnName:jdd-rec-moto]");
        Assertions.assertTrue(list.size()==1);
    }

    @Test
    void testMatchWithOnlyDataReference() {
        List<String> list = RegexUtil.match(VALUE_REFERENCE_REGEX," Mon numéroclient est le <<<num_client>>>  et mon numéro contrat est la <<<num_contrat>>>");
        Assertions.assertTrue(list.contains("<<<num_contrat>>>"));
        Assertions.assertTrue(list.contains("<<<num_client>>>"));
    }
}