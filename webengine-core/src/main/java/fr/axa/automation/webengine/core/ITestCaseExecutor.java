package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.ITestCaseContext;
import fr.axa.automation.webengine.generated.TestCaseReport;

public interface ITestCaseExecutor  {

    ITestCaseContext initialize(GlobalApplicationContext globalApplicationContext, String testCaseName, ITestCase testCase) throws WebEngineException;

    TestCaseReport run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException;

    void cleanUp(ITestCaseContext testCaseContext) throws WebEngineException;
}
