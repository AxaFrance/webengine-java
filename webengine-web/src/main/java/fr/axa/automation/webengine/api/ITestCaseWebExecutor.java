package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCase;
import fr.axa.automation.webengine.core.ITestCaseExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;

public interface ITestCaseWebExecutor extends ITestCaseExecutor {
    AbstractTestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, String testCaseName, ITestCase testCase) throws WebEngineException;
}
