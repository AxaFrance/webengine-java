package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.DriverContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenCommand extends AbstractDriverCommand{
    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        String url = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        minimizeCurrentWindow(commandResultList);
        WebDriver newWebDriver = instantiateWebDriver(globalApplicationContext, commandResultList);
        DriverContext newDriverContext = getDriverContext(newWebDriver, url);
        setDriverContext(newDriverContext);
        newWebDriver = newDriverContext.getWebDriver();
        newWebDriver.switchTo().window(newDriverContext.getWindowHandle());
        newWebDriver.manage().window().maximize();
        newWebDriver.navigate().to(url);
    }

    protected void minimizeCurrentWindow(List<CommandResult> commandResultList) throws WebEngineException {
        WebDriver currentWebDriver = getWebDriverToUse(commandResultList);
        if(currentWebDriver != null){
            currentWebDriver.manage().window().minimize();
        }
    }

    protected DriverContext getDriverContext(WebDriver webDriver, String url) {
        Map<String, String> sessionIdAndUrlMap = new HashMap<>();
        sessionIdAndUrlMap.put(webDriver.getWindowHandle(), url);
        return DriverContext.builder().currentUrl(url).webDriver(webDriver).sessionIdAndUrlMap(sessionIdAndUrlMap).build();
    }

    protected WebDriver instantiateWebDriver(AbstractGlobalApplicationContext globalApplicationContext,List<CommandResult> commandResultList) throws WebEngineException {
        return initializeWebDriver(globalApplicationContext,false);
    }
}
