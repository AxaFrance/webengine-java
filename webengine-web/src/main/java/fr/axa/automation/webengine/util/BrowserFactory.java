package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.BrowserType;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public class BrowserFactory {

    public static Optional<WebDriver> getDriver(Settings settings) throws WebEngineException {
        Optional<WebDriver> webDriver = Optional.empty();
        if(settings.getPlatform()== Platform.WINDOWS){
            if(settings.getBrowserType()== BrowserType.CHROME){
                webDriver = ChromeDriverUtil.getChromeDriver();
            }else if(settings.getBrowserType()== BrowserType.CHROMIUM_EDGE){
                webDriver = EdgeDriverUtil.getEdgeDriver();
            }else if(settings.getBrowserType()== BrowserType.FIREFOX){
                webDriver = FirefoxDriverUtil.getFirefoxDriver();
            }
            if(webDriver.isPresent()){
                webDriver.get().manage().deleteAllCookies();
            }
        }
        return webDriver;
    }
}
