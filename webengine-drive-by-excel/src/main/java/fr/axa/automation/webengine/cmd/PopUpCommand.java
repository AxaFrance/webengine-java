package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.Constant;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class PopUpCommand extends AbstractDriverCommand {
    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        WebDriver webDriver = getWebDriverToUse(commandResultList);
        String value = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        if (webDriver != null) {
            if(StringUtil.equalsIgnoreCase(value, Constant.OUI.getValue()) || StringUtil.equalsIgnoreCase(value, Constant.OK.getValue())){
                webDriver.switchTo().alert().accept();
            } else{
                webDriver.switchTo().alert().dismiss();
            }
        }else {
            throw new Exception("WebDriver is null");
        }
    }
}
