package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestCaseReport;

public interface ITestCaseExecutor  {
    default ITestCaseContext initialize(GlobalApplicationContext globalApplicationContext) throws WebEngineException{
        return null;
    }

    TestCaseReport run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException;

    void cleanUp(ITestCaseContext testCaseContext) throws WebEngineException;
}
