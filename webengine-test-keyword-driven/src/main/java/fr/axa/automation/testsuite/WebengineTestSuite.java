package fr.axa.automation.testsuite;

import fr.axa.automation.testcase.FlowTestCase;
import fr.axa.automation.testcase.SimpleTestCase;
import fr.axa.automation.webengine.core.AbstractTestSuite;
import fr.axa.automation.webengine.core.ITestCase;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class WebengineTestSuite extends AbstractTestSuite {

    public static final String TEST_CASE_1 = "TEST_CASE_1";
    public static final String TEST_CASE_2 = "TEST_CASE_2";
    public static final String TEST_CASE_3 = "TEST_CASE_3";

    @Override
    public List<AbstractMap.SimpleEntry<String, ? extends ITestCase>> getTestCaseList() {
        List<AbstractMap.SimpleEntry<String, ? extends ITestCase>> testCaseList = new ArrayList<>();
        testCaseList.add(new AbstractMap.SimpleEntry<String, ITestCase>(TEST_CASE_1,new FlowTestCase()));
        testCaseList.add(new AbstractMap.SimpleEntry<String, ITestCase>(TEST_CASE_2,new SimpleTestCase())); //--Doesn't run because it's not present in testData file
        testCaseList.add(new AbstractMap.SimpleEntry<String, ITestCase>(TEST_CASE_3,new SimpleTestCase())); //--Doesn't run because it's not present in Argument line, cf Class ApplicationTest
        return testCaseList;
    }
}
