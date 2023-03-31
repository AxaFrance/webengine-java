package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class ScrenshotCommand extends AbstractDriverCommand{

    @Override
    public Object executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws Exception {
       screenShot(testCaseContext,"");
       return null;
    }



}
