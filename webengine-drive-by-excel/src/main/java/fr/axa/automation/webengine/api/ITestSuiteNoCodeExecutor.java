package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;

import java.net.UnknownHostException;

public interface ITestSuiteNoCodeExecutor extends ITestSuiteExecutor {
    TestSuiteReport run(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData) throws WebEngineException, UnknownHostException;
}
