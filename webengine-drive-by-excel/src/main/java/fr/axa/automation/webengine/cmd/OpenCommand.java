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
import fr.axa.automation.webengine.util.StringUtil;
import fr.axa.automation.webengine.util.UriUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        String url = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        WebDriver webDriver = null;

        DriverContext currentDriverContext = getWebDriverFromUrl(commandResultList, url);
        if(currentDriverContext==null) {
            currentDriverContext = getWindowHandlesInDriver(commandResultList, url);
            if(currentDriverContext==null) {
                webDriver = instantiateWebDrive(globalApplicationContext);
                Map<String, String> sessionIdAndUrlMap = new HashMap<>();
                sessionIdAndUrlMap.put(webDriver.getWindowHandle(), url);
                currentDriverContext = DriverContext.builder().currentUrl(url).webDriver(webDriver).sessionIdAndUrlMap(sessionIdAndUrlMap).build();
            }
        }

        setDriverContext(currentDriverContext);
        webDriver = currentDriverContext.getWebDriver();
        String originalWindow = currentDriverContext.getWindowHandle();
        webDriver.switchTo().window(originalWindow);
        webDriver.manage().window().maximize();
        webDriver.navigate().to(url);
    }

    protected WebDriver instantiateWebDrive(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        return initializeWebDriver(globalApplicationContext);
    }

    private DriverContext getWebDriverFromUrl(List<CommandResult> commandResultList, String url) throws WebEngineException {
        List<DriverContext> driverContextList = CommandResultHelper.getWebDriverList(commandResultList);
        if (CollectionUtils.isNotEmpty(driverContextList)) { //Dans le cas ou l'application a déjà été ouverte, on réutilise le même driver
            for (DriverContext driverContext : driverContextList) {
                if (StringUtil.equalsIgnoreCase(driverContext.getWebDriver().getCurrentUrl(), "data:,") || StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(driverContext.getCurrentUrl()), UriUtil.getHostFromURI(url))) {
                    return driverContext;
                }
            }
        }
        return null;
    }

    private DriverContext getWindowHandlesInDriver(List<CommandResult> commandResultList, String url) throws WebEngineException {
        List<DriverContext> driverContextList = CommandResultHelper.getWebDriverList(commandResultList);
        if (CollectionUtils.isNotEmpty(driverContextList)) {
            List<DriverContext> driverContextListFilter = driverContextList.stream().filter(currentDriverContext -> currentDriverContext.getWebDriver().getWindowHandles().size() != currentDriverContext.getSessionIdAndUrlMap().size()).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(driverContextListFilter)){
                for (DriverContext currentDriverContext : driverContextList) {
                    for (Map.Entry<String,String> entry : currentDriverContext.getSessionIdAndUrlMap().entrySet()) {
                        if(StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(entry.getValue()),UriUtil.getHostFromURI(url))){
                            return DriverContext.builder().currentUrl(url).webDriver(currentDriverContext.getWebDriver()).sessionIdAndUrlMap(currentDriverContext.getSessionIdAndUrlMap()).build();
                        }
                    }
                }
            }else{
                if (driverContextListFilter.size() > 1) {
                    throw new WebEngineException("too many drivers context have found");
                }

                DriverContext driverContextFound = driverContextListFilter.get(0);
                Map<String, String> sessionIdAndUrlMap = driverContextFound.getSessionIdAndUrlMap();
                sessionIdAndUrlMap.put(ListUtil.getLastElement(driverContextFound.getWebDriver().getWindowHandles()).get(),url );
                return DriverContext.builder().currentUrl(url).webDriver(driverContextFound.getWebDriver()).sessionIdAndUrlMap(sessionIdAndUrlMap).build();
            }
        }
        return null;
    }
}
