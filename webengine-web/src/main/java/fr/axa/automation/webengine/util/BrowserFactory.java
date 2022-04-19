package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.BrowserType;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import org.openqa.selenium.WebDriver;

public class BrowserFactory {

    public static WebDriver getDriver(Settings settings) throws WebEngineException {
        WebDriver webDriver = null;
        if(settings.getPlatform()== Platform.WINDOWS){
            if(settings.getBrowserType()== BrowserType.CHROME){
                webDriver = ChromeDriverUtil.getChromeDriver();
            }else if(settings.getBrowserType()== BrowserType.CHROMIUM_EDGE){
                webDriver = EdgeDriverUtil.getEdgeDriver();
            }else if(settings.getBrowserType()== BrowserType.FIREFOX){
                webDriver = FirefoxDriverUtil.getFirefoxDriver();
            }
            webDriver.manage().deleteAllCookies();
        }
        return webDriver;
    }
}
