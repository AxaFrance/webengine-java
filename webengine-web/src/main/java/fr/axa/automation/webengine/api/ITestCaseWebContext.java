package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCase;
import fr.axa.automation.webengine.core.ITestCaseContext;

public interface ITestCaseWebContext extends ITestCaseContext {
    void setTestCaseToExecute(ITestCase testCaseToExecute);
    ITestCase getTestCaseToExecute();
}
