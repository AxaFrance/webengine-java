package fr.axa.automation.webengine.api;

import fr.axa.automation.webengine.core.ITestCaseContext;
import fr.axa.automation.webengine.core.ITestStepExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public interface ITestStepDriveByExcelExecutor extends ITestStepExecutor {

    ActionReport run(AbstractGlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws WebEngineException;
}
