package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;

import java.net.UnknownHostException;

public interface ITestSuiteDriveByExcelExecutor extends ITestSuiteExecutor {
    TestSuiteReport run(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataDriveByExcel testSuiteData) throws WebEngineException, UnknownHostException;
}
