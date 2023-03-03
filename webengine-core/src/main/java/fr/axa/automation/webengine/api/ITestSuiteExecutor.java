package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestSuiteReport;

import java.net.UnknownHostException;

public interface ITestSuiteExecutor extends ITestExecutor {
    default TestSuiteReport run(GlobalApplicationContext globalApplicationContext) throws WebEngineException, UnknownHostException{
        return null;
    }
}
