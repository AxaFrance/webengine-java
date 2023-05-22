package fr.axa.automation.webengine.general;

import fr.axa.automation.webengine.core.ITestCase;

public interface ITestCaseContext {

    void setTestCaseName(String testCaseName);
    String getTestCaseName();
    void setTestCaseToExecute(ITestCase testCaseToExecute);
    ITestCase getTestCaseToExecute();

    Object getWebDriver();

}
