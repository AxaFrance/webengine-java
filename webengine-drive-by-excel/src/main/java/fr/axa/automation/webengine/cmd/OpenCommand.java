package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.helper.EvaluateValueHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class OpenCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        String url = commandData.getTargetList().get(TargetKey.OPEN);
        url = EvaluateValueHelper.evaluateValue(globalApplicationContext.getSettings(),url,commandResultList);
        WebDriver webDriver = (WebDriver)testCaseContext.getWebDriver();
        webDriver.manage().window().maximize();
        webDriver.navigate().to(url);
    }
}
