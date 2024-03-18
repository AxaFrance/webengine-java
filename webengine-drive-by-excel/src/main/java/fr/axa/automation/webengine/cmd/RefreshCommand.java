package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class RefreshCommand extends AbstractDriverCommand {
    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        WebDriver webDriver = getWebDriverToUse(commandResultList);
        if (webDriver != null) {
            webDriver.navigate().refresh();
        }else {
            throw new Exception("WebDriver is null");
        }
    }
}
