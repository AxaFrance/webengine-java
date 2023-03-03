package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseContext;
import fr.axa.automation.webengine.core.ITestCaseExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.object.TestCaseData;

public interface ITestCaseDriveByExcelExecutor extends ITestCaseExecutor {
    ITestCaseContext initialize(GlobalApplicationContext globalApplicationContext, TestCaseData testCaseData) throws WebEngineException;

    TestCaseReport run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException;
}
