package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseContext;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;

public interface ITestCaseDriveByExcelContext extends ITestCaseContext {
    void setTestCaseData(TestCaseDataDriveByExcel testCaseData);
    TestCaseDataDriveByExcel getTestCaseData();
}
