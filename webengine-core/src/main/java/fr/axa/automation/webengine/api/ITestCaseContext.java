package fr.axa.automation.webengine.api;

public interface ITestCaseContext {
    void setTestCaseName(String testCaseName);
    String getTestCaseName();
    Object getWebDriver();
    void setWebDriver(Object webDriver);
}
