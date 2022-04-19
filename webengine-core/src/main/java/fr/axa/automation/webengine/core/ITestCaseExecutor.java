package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestCaseReport;

public interface ITestCaseExecutor extends ITestExecutor {
    TestCaseReport run(GlobalApplicationContext globalApplicationContext, String testCaseName, ITestCase testCase) throws WebEngineException;
}
