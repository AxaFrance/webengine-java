package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;

public interface ITestCaseWebExecutor extends ITestCaseExecutor{
    ITestCaseContext initialize(GlobalApplicationContext globalApplicationContext, String testCaseName, ITestCase testCase) throws WebEngineException;
}
