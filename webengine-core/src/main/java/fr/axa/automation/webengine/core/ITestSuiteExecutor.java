package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;

import java.net.UnknownHostException;

public interface ITestSuiteExecutor extends ITestExecutor {
    default TestSuiteReport run(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException, UnknownHostException{
        return null;
    }
}
