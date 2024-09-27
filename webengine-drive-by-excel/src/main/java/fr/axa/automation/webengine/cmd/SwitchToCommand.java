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
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SwitchToCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        String urlOrid = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        if(StringUtils.isEmpty(urlOrid)){
            throw new WebEngineException("you must define a window id to switch to");
        }

        DriverContext currentDriverContext = getWebDriverContextFromUrl(commandResultList, urlOrid);
        if(currentDriverContext==null) {
            currentDriverContext = getWindowHandlesInDriver(commandResultList, urlOrid);
            if(currentDriverContext==null) {
                throw new WebEngineException("No driver context have found for this id");
            }
        }

        setDriverContext(currentDriverContext);
        WebDriver webDriver = currentDriverContext.getWebDriver();
        String windowToSwitch = currentDriverContext.getWindowHandle();
        webDriver.switchTo().window(windowToSwitch);
        webDriver.manage().window().maximize();
    }

    protected DriverContext getWebDriverContextFromUrl(List<CommandResult> commandResultList, String urlOrid) throws WebEngineException {
        List<DriverContext> driverContextList = CommandResultHelper.getWebDriverList(commandResultList);
        if (CollectionUtils.isNotEmpty(driverContextList)) { //If application is already open , we use the same driver
            List<DriverContext> driverContextListFilter = driverContextList.stream().filter(currentDriverContext -> currentDriverContext.getWebDriver().getWindowHandles().size() != currentDriverContext.getSessionIdAndUrlMap().size()).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(driverContextListFilter)) {
                for (DriverContext driverContext : driverContextListFilter) {
                    String urlOrId1 = UriUtil.getHostFromURI(driverContext.getCurrentUrl()) == null ? driverContext.getCurrentUrl() : UriUtil.getHostFromURI(driverContext.getCurrentUrl());
                    String urlOrId2 = UriUtil.getHostFromURI(urlOrid) == null ? urlOrid : UriUtil.getHostFromURI(urlOrid);
                    if (StringUtil.equalsIgnoreCase(urlOrId1, urlOrId2)) {
                        return driverContext;
                    }
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
                        String urlOrId1 = UriUtil.getHostFromURI(entry.getValue()) == null ? entry.getValue() : UriUtil.getHostFromURI(entry.getValue());
                        String urlOrId2 = UriUtil.getHostFromURI(urlOrId) == null ? urlOrId : UriUtil.getHostFromURI(urlOrId);
                        if (StringUtil.equalsIgnoreCase(urlOrId1, urlOrId2)) {
                            return DriverContext.builder().currentUrl(urlOrId).webDriver(currentDriverContext.getWebDriver()).sessionIdAndUrlMap(currentDriverContext.getSessionIdAndUrlMap()).build();
                        }
                    }
                }
            }else{
                List<WebDriver> webDrivers = driverContextListFilter.stream().map(currentDriverContext -> currentDriverContext.getWebDriver()).distinct().collect(Collectors.toList());
                if (webDrivers.size() > 1) {
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
