package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public final class WebdriverHelper {

    private WebdriverHelper() {
    }

    public static WebDriver initializeDriver() throws Exception {
        Optional<WebDriver> driver = getDriver();
        if(driver.isPresent()){
            return driver.get();
        }else{
            throw new Exception("Error during get driver");
        }
    }

    public static Optional<WebDriver> getDriver() throws Exception {
        Optional<GlobalConfigProperties> optionalGlobalConfigProperties = getConfig();
        Optional<WebDriver> driver;
        if(optionalGlobalConfigProperties.isPresent()){
            driver = BrowserFactory.getDriver(optionalGlobalConfigProperties.get());
        }else{
            driver = getDefaultDriver();
        }
        return driver;
    }

    private static Optional<GlobalConfigProperties> getConfig() throws Exception {
        return PropertiesHelperProvider.getInstance().getDefaultGlobalConfiguration();
    }

    public static Optional<WebDriver> getDefaultDriver() throws WebEngineException {
        return BrowserFactory.getWebDriver(Platform.WINDOWS, Browser.CHROMIUM_EDGE);
    }

    public static void quiDriver(WebDriver webDriver) throws WebEngineException {
        try {
            webDriver.quit();
        }catch (Exception e){
            throw  new WebEngineException("Warning during quit browser",e);
        }
    }
}
