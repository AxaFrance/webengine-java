package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;

public interface ITestCaseExecutor extends ITestExecutor{

    TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException;

}
