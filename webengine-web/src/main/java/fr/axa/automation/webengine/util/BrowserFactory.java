package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.BrowserType;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public class BrowserFactory {

    public static Optional<WebDriver> getDriver(Settings settings) throws WebEngineException {
        return getDriver(settings.getPlatform(),settings.getBrowserType());
    }

    public static Optional<WebDriver> getDriver(Platform platform, BrowserType browserType) throws WebEngineException {
        Optional<WebDriver> webDriver = Optional.empty();
        if(platform== Platform.WINDOWS){
            if(browserType== BrowserType.CHROME){
                webDriver = ChromeDriverUtil.getChromeDriver();
            }else if(browserType== BrowserType.CHROMIUM_EDGE){
                webDriver = EdgeDriverUtil.getEdgeDriver();
            }else if(browserType== BrowserType.FIREFOX){
                webDriver = FirefoxDriverUtil.getFirefoxDriver();
            }
            if(webDriver.isPresent()){
                webDriver.get().manage().deleteAllCookies();
            }
        }
        return webDriver;
    }

}
