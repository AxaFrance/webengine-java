package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.CommandResultHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import fr.axa.automation.webengine.util.UriUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class OpenCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        String url = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        WebDriver webDriver = null;

        List<WebDriver> webDriverList = CommandResultHelper.getWebDriverList(commandResultList);
        if(CollectionUtils.isNotEmpty(webDriverList)){//Dans le cas ou l'application a déjà été ouvert, on reutilise le même driver
            for ( WebDriver webDriverStored : webDriverList ) {
                if(StringUtil.equalsIgnoreCase(webDriverStored.getCurrentUrl(),"data:,") || StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(webDriverStored.getCurrentUrl()),UriUtil.getHostFromURI(url))){
                    webDriver = webDriverStored;
                }
            }
        }
        if(webDriver==null) {
            webDriver = instantiateWebDrive(globalApplicationContext);
        }
        setWebDriver(webDriver);
        String originalWindow = webDriver.getWindowHandle();
        webDriver.switchTo().window(originalWindow);
        webDriver.manage().window().maximize();
        webDriver.navigate().to(url);
    }

    protected WebDriver instantiateWebDrive(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        return initializeWebDriver(globalApplicationContext);
    }
}
