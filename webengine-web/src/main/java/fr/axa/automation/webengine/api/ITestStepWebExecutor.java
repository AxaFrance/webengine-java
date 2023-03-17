package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseContext;
import fr.axa.automation.webengine.core.ITestStep;
import fr.axa.automation.webengine.core.ITestStepExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.report.object.ActionReportDetail;

public interface ITestStepWebExecutor extends ITestStepExecutor {
    ActionReportDetail run(AbstractGlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext, ITestStep testStep) throws WebEngineException;
}
