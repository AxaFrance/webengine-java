package fr.axa.automation.webengine.core;

import java.util.Map;

public interface ITestSuite {
   Map<String,? extends ITestCase> getTestCaseList();
}
