package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.DriverContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.CommandResultHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenTabCommand extends OpenCommand {

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        String url = getValue(globalApplicationContext, (TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        if (StringUtils.isEmpty(url)) {
            throw new WebEngineException("you must define a tab url to open a tab");
        }

        DriverContext currentDriverContext = getWebDriverContextFromUrl(commandResultList, url);

        setDriverContext(currentDriverContext);
        WebDriver webDriver = currentDriverContext.getWebDriver();
        String windowToSwitch = currentDriverContext.getWindowHandle();
        webDriver.switchTo().window(windowToSwitch);
        webDriver.manage().window().maximize();
        webDriver.navigate().to(url);
    }

    protected DriverContext getWebDriverContextFromUrl(List<CommandResult> commandResultList, String url) throws WebEngineException {
        List<DriverContext> driverContextList = CommandResultHelper.getWebDriverList(commandResultList);
        DriverContext lastDriverContext = ListUtil.getLastElement(driverContextList.stream().collect(Collectors.toList())).get();
        WebDriver webDriver = lastDriverContext.getWebDriver();
        ((JavascriptExecutor) webDriver).executeScript("window.open()");

        Map<String, String> sessionIdAndUrlMap = lastDriverContext.getSessionIdAndUrlMap();
        sessionIdAndUrlMap.put(ListUtil.getLastElement(lastDriverContext.getWebDriver().getWindowHandles()).get(),url );
        return DriverContext.builder().currentUrl(url).webDriver(lastDriverContext.getWebDriver()).sessionIdAndUrlMap(sessionIdAndUrlMap).build();
    }
}
