package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.Browser;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public final class WebdriverHelper {

    private WebdriverHelper() {
    }

    public static WebDriver initializeWebDriver(String profile) throws Exception {
        Optional<WebDriver> webDriver = getWebDriver(profile);
        if(webDriver.isPresent()){
            return webDriver.get();
        }else{
            throw new Exception("Error during get webDriver");
        }
    }

    public static WebDriver initializeWebDriver() throws Exception {
       return initializeWebDriver(null);
    }

    public static Optional<WebDriver> getWebDriver(String profile) throws Exception {
        Optional<WebDriver> webDriver;
        Optional<GlobalConfiguration> optionalGlobalConfigProperties = getGlobalConfigurationByProfileOrLocalOrDefault(profile);
        if(optionalGlobalConfigProperties.isPresent()){
            webDriver = BrowserFactory.getDriver(optionalGlobalConfigProperties.get());
        }else{
            webDriver = getDefaultWebDriver();
        }
        return webDriver;
    }

    public static Optional<GlobalConfiguration> getGlobalConfigurationByProfileOrLocalOrDefault(String profile) throws Exception {
        return PropertiesHelperProvider.getInstance().getGlobalConfigurationByProfileOrLocalOrDefault(profile);
    }

    public static Optional<WebDriver> getDefaultWebDriver() throws WebEngineException {
        return BrowserFactory.getWebDriver(Platform.WINDOWS, Browser.CHROMIUM_EDGE,true);
    }

    public static void quitWebDriver(WebDriver webDriver) throws WebEngineException {
        try {
            webDriver.quit();
        }catch (Exception e){
            throw  new WebEngineException("Warning during quit browser",e);
        }
    }
}
