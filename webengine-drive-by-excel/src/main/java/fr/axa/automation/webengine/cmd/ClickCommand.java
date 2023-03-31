package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class ClickCommand extends AbstractDriverCommand{

    @Override
    public Object executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        webElementDescription.click();
        return null;
    }
}
