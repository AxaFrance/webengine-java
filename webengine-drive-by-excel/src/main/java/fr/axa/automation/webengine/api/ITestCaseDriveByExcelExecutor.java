package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseNodeDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;

import java.util.List;

public interface ITestCaseDriveByExcelExecutor extends ITestCaseExecutor {
    AbstractTestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, TestCaseNodeDriveByExcel testCaseToRun, TestSuiteDataDriveByExcel testSuiteData) throws WebEngineException;

    TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException;
}
