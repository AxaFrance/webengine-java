package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class SwitchFrameCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        String value = getValue(globalApplicationContext, (TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        WebDriver webDriver = getWebDriverToUse(globalApplicationContext,testCaseContext,commandResultList);
        if (webDriver != null) {
            webDriver.switchTo().frame(value);
        }
    }
}