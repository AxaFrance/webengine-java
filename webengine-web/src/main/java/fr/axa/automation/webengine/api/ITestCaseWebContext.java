package fr.axa.automation.webengine.api;

public interface ITestCaseWebContext extends ITestCaseContext{
    void setTestCaseToExecute(ITestCase testCaseToExecute);
    ITestCase getTestCaseToExecute();
}
