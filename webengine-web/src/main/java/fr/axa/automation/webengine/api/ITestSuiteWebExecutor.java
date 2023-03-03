package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestSuiteReport;

import java.net.UnknownHostException;

public interface ITestSuiteWebExecutor extends ITestSuiteExecutor {
    TestSuiteReport run(GlobalApplicationContext globalApplicationContext, ITestSuite testSuite) throws WebEngineException, UnknownHostException;
}
