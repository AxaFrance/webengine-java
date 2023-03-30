package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public interface ICommand {
    Object execute(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData)throws Exception;
}
