package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.report.object.ActionReportDetail;

public interface ITestStepExecutor extends ITestExecutor {
    ActionReportDetail run(GlobalApplicationContext globalApplicationContext, Object context, String testCaseName, ITestStep testStep) throws WebEngineException;
}
