package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;

import java.net.UnknownHostException;

public interface ITestSuiteExecutorDriveByExcel extends ITestSuiteExecutor {
    TestSuiteReport run(GlobalApplicationContext globalApplicationContext, AbstractTestSuiteData testSuiteData) throws WebEngineException, UnknownHostException;
}
