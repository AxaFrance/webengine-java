package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import org.openqa.selenium.WebDriver;

public class OpenCommand extends AbstractDriverCommand{

    @Override
    public Object executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws Exception {
        ((WebDriver)testCaseContext.getWebDriver()).navigate().to(commandData.getTargetList().get(CommandName.OPEN.getName()));
        return null;
    }
}
