package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestSuiteReport;

import java.net.UnknownHostException;

public interface ITestSuiteExecutor extends ITestExecutor {
    TestSuiteReport run(GlobalApplicationContext globalApplicationContext, ITestSuite testSuite) throws WebEngineException, UnknownHostException;
}
