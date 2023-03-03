package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.report.object.ActionReportDetail;

public interface ITestStepExecutor extends ITestExecutor {
    ActionReportDetail run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext, ITestStep testStep) throws WebEngineException;
}
