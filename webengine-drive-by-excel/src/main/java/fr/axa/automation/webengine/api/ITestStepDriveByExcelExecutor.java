package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseContext;
import fr.axa.automation.webengine.core.ITestStepExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.object.CommandData;

public interface ITestStepDriveByExcelExecutor extends ITestStepExecutor {

    ActionReport run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext, CommandData commandData) throws WebEngineException;
}
