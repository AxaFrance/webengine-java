package fr.axa.automation.testsuite;

import com.google.common.collect.ImmutableMap;
import fr.axa.automation.testcase.FlowTestCase;
import fr.axa.automation.testcase.SimpleTestCase;
import fr.axa.automation.webengine.core.AbstractTestSuite;
import fr.axa.automation.webengine.core.ITestCase;

import java.util.Map;

public class WebengineTestSuite extends AbstractTestSuite {
    public static final String TEST_CASE_1 = "TEST_CASE_1";
    public static final String TEST_CASE_2 = "TEST_CASE_2";
    public static final String TEST_CASE_3 = "TEST_CASE_3";

    public Map<String, ? extends ITestCase> getTestCaseList() {
        return ImmutableMap.of( TEST_CASE_1, new FlowTestCase(),
                                TEST_CASE_2, new SimpleTestCase(),
                                TEST_CASE_3, new SimpleTestCase());
    }
}
