package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class PopUpCommand extends AbstractDriverCommand {
    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        WebDriver driver = (WebDriver) testCaseContext.getWebDriver();
        String value = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);

        if (driver != null) {
            if ("oui".equalsIgnoreCase(value)||"ok".equalsIgnoreCase(value)){
                driver.switchTo().alert().accept();
            }
            else {
                driver.switchTo().alert().dismiss();
            }
        }
    }
}
