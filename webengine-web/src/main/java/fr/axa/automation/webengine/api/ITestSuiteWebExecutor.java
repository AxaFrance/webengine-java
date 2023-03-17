package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;

import java.net.UnknownHostException;

public interface ITestSuiteWebExecutor extends ITestSuiteExecutor {
    TestSuiteReport run(AbstractGlobalApplicationContext globalApplicationContext, ITestSuite testSuite) throws WebEngineException, UnknownHostException;
}
