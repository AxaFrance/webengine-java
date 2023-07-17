package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.helper.CommandResultHelper;
import fr.axa.automation.webengine.helper.EvaluateValueHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.ListUtil;
import fr.axa.automation.webengine.util.StringUtil;
import fr.axa.automation.webengine.util.UriUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class OpenCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        String url = commandData.getTargetList().get(TargetKey.OPEN);
        url = EvaluateValueHelper.evaluateValue(globalApplicationContext.getSettings(),url,commandResultList);
        WebDriver webDriver = null;
        List<WebDriver> webDriverList = CommandResultHelper.getWebDriverByOpenCommand(commandResultList);
        if(CollectionUtils.isEmpty(webDriverList)){
            webDriver = getLastWebDriver(commandResultList);
        }else{
            for ( WebDriver webDriverStored : webDriverList ) {
                if(StringUtil.equalsIgnoreCase(webDriverStored.getCurrentUrl(),"data:,") || StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(webDriverStored.getCurrentUrl()),UriUtil.getHostFromURI(url))){
                    webDriver = webDriverStored;
                }
            }
        }
        if(webDriver==null) {
            webDriver = initializeWebDriver(globalApplicationContext);
        }
        setWebDriver(webDriver);
        String originalWindow = webDriver.getWindowHandle();
        webDriver.switchTo().window(originalWindow);
        webDriver.manage().window().maximize();
        webDriver.navigate().to(url);
    }
}
