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
import org.openqa.selenium.WindowType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        String urlOrId = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        WebDriver webDriver ;
        boolean navigateToUrl = true;

        DriverContext currentDriverContext = getWebDriverFromUrl(commandResultList, urlOrId);
        if(currentDriverContext==null) {
            currentDriverContext = getWindowHandlesInDriver(commandResultList, urlOrId);
            if(currentDriverContext==null) {
                webDriver = instantiateWebDriver(globalApplicationContext);
                Map<String, String> sessionIdAndUrlMap = new HashMap<>();
                sessionIdAndUrlMap.put(webDriver.getWindowHandle(), urlOrId);
                currentDriverContext = DriverContext.builder().currentUrl(urlOrId).webDriver(webDriver).sessionIdAndUrlMap(sessionIdAndUrlMap).build();
            }else{
                navigateToUrl = false;
            }
        }else{
            navigateToUrl = false;
        }

        setDriverContext(currentDriverContext);
        webDriver = currentDriverContext.getWebDriver();
        String originalWindow = currentDriverContext.getWindowHandle();
        webDriver.switchTo().newWindow(getWindowType());
        webDriver.switchTo().window(originalWindow);
        webDriver.manage().window().maximize();
        if(navigateToUrl){
            webDriver.navigate().to(urlOrId);
        }
    }

    protected WindowType getWindowType() {
        return WindowType.WINDOW;
    }

    protected WebDriver instantiateWebDriver(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        return initializeWebDriver(globalApplicationContext,false);
    }

    protected DriverContext getWebDriverFromUrl(List<CommandResult> commandResultList, String url) throws WebEngineException {
        List<DriverContext> driverContextList = CommandResultHelper.getWebDriverList(commandResultList);
        if (CollectionUtils.isNotEmpty(driverContextList)) { //Dans le cas ou l'application a déjà été ouverte, on réutilise le même driver
            for (DriverContext driverContext : driverContextList) {
                if (StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(driverContext.getCurrentUrl()), UriUtil.getHostFromURI(url))) {
                    return driverContext;
                }
            }
        }
        return null;
    }

    protected DriverContext getWindowHandlesInDriver(List<CommandResult> commandResultList, String urlOrId) throws WebEngineException {
        List<DriverContext> driverContextList = CommandResultHelper.getWebDriverList(commandResultList);
        if (CollectionUtils.isNotEmpty(driverContextList)) {
            List<DriverContext> driverContextListFilter = driverContextList.stream().filter(currentDriverContext -> currentDriverContext.getWebDriver().getWindowHandles().size() != currentDriverContext.getSessionIdAndUrlMap().size()).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(driverContextListFilter)){
                for (DriverContext currentDriverContext : driverContextList) {
                    for (Map.Entry<String,String> entry : currentDriverContext.getSessionIdAndUrlMap().entrySet()) {
                        if(StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(entry.getValue()),UriUtil.getHostFromURI(urlOrId))){
                            return DriverContext.builder().currentUrl(urlOrId).webDriver(currentDriverContext.getWebDriver()).sessionIdAndUrlMap(currentDriverContext.getSessionIdAndUrlMap()).build();
                        }
                    }
                }
            }else{
                if (driverContextListFilter.size() > 1) {
                    throw new WebEngineException("too many drivers context have found");
                }

                DriverContext driverContextFound = driverContextListFilter.get(0);
                Map<String, String> sessionIdAndUrlMap = driverContextFound.getSessionIdAndUrlMap();
                sessionIdAndUrlMap.put(ListUtil.getLastElement(driverContextFound.getWebDriver().getWindowHandles()).get(),urlOrId );
                return DriverContext.builder().currentUrl(urlOrId).webDriver(driverContextFound.getWebDriver()).sessionIdAndUrlMap(sessionIdAndUrlMap).build();
            }
        }
        return null;
    }
}
