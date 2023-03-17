package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseContext;
import fr.axa.automation.webengine.core.ITestCaseExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;

public interface ITestCaseDriveByExcelExecutor extends ITestCaseExecutor {
    ITestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, TestCaseDataDriveByExcel testCaseData) throws WebEngineException;

    TestCaseReport run(AbstractGlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException;
}
