package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.object.TestCaseData;

public interface ITestCaseDriveByExcelContext extends ITestCaseContext{
    void setTestCaseData(TestCaseData testCaseData);
    TestCaseData getTestCaseData();
}
