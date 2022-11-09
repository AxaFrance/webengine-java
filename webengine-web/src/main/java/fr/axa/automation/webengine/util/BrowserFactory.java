package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.properties.AppiumSettingsProperties;
import fr.axa.automation.webengine.properties.ApplicationProperties;
import fr.axa.automation.webengine.properties.CapabilitiesProperties;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.collections4.MapUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BrowserFactory {

    private static GlobalConfigProperties getGlobalConfig(Settings settings) throws WebEngineException {
        Optional<GlobalConfigProperties> globalConfigProperties = PropertiesUtilV2.getInstance().getGlobalConfiguration(settings.getPropertiesFileList(),PropertiesUtilV2.APPLICATION_FILE_NAME_WITHOUT_POSTFIX);
        if(!globalConfigProperties.isPresent()){
            globalConfigProperties = Optional.of(GlobalConfigProperties.builder().build());
        }
        ApplicationProperties applicationProperties = ApplicationProperties.builder().platformName(settings.getPlatform().name()).browserName(settings.getBrowser().name()).build();
        globalConfigProperties.get().setApplication(applicationProperties);
        return globalConfigProperties.get();
    }

    public static Optional<WebDriver> getDriver(Settings settings) throws WebEngineException {
        GlobalConfigProperties globalConfigProperties = getGlobalConfig(settings);
        Platform platform = Platform.valueOf(globalConfigProperties.getApplication().getPlatformName());
        if(platform == Platform.WINDOWS){
            return getDesktopDriver(globalConfigProperties);
        }else if(platform == Platform.ANDROID || platform == Platform.IOS){
            return getAppiumDriver(globalConfigProperties);
        }else{
            throw new WebEngineException("Not recognized the 'platform' parameter.");
        }
    }

    public static Optional<WebDriver> getDriver(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Platform platform = Platform.valueOf(globalConfigProperties.getApplication().getPlatformName());
        if(platform == Platform.WINDOWS){
            return getDesktopDriver(globalConfigProperties);
        }else if(platform == Platform.ANDROID || platform == Platform.IOS){
            return getAppiumDriver(globalConfigProperties);
        }else{
            throw new WebEngineException("Not recognized the 'platform' parameter.");
        }
    }

    public static Optional<WebDriver> getDesktopDriver(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Platform platform = Platform.valueOf(globalConfigProperties.getApplication().getPlatformName());
        Browser browser = Browser.valueOf(globalConfigProperties.getApplication().getBrowserName());
        return getWebDriver(platform, browser);
    }

    public static Optional<WebDriver> getWebDriver(Platform platform, Browser browser) throws WebEngineException {
        Optional<WebDriver> webDriver = Optional.empty();
        if(platform == Platform.WINDOWS){
            if(browser == Browser.CHROME){
                webDriver = ChromeDriverUtil.getChromeDriver();
            }else if(browser == Browser.CHROMIUM_EDGE){
                webDriver = EdgeDriverUtil.getEdgeDriver();
            }else if(browser == Browser.FIREFOX){
                webDriver = FirefoxDriverUtil.getFirefoxDriver();
            }
            webDriver.ifPresent(driver -> driver.manage().deleteAllCookies());
        }
        return webDriver;
    }

    public static <T extends WebDriver> Optional<T> getAppiumDriver(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Platform platform = Platform.valueOf(globalConfigProperties.getApplication().getPlatformName());
        try {
            AppiumSettingsProperties appiumSettings = globalConfigProperties.getAppiumSettings();
            if(appiumSettings!=null){
                if(platform == Platform.ANDROID) {
                    return (Optional<T>) Optional.of(new AndroidDriver(new URL(getURLBrowserStack(appiumSettings)), getAppiumOption(globalConfigProperties)));
                }else if(platform == Platform.IOS){
                    return (Optional<T>) Optional.of(new IOSDriver(new URL(getURLBrowserStack(appiumSettings)), getAppiumOption(globalConfigProperties)));
                } else {
                    throw new WebEngineException("Platform not recognized for getting Appium driver");
                }
            }else{
                throw  new WebEngineException("Appium settings are null. Need the 'application-properties.file' property file ");
            }
        } catch (MalformedURLException e) {
            throw  new WebEngineException("Error during getting Appium driver",e);
        }
    }

    private static DesiredCapabilities getAppiumOption(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Browser browser = Browser.valueOf(globalConfigProperties.getApplication().getBrowserName());
//        Platform platform = Platform.valueOf(globalConfigProperties.getApplication().getPlatformName());
//        String automationName = platform == Platform.ANDROID ? "UiAutomator2" : "Safari";

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME,browser.getValue());

//        desiredCapabilities.setCapability("appium:platformName",platform.getValue());
//        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,automationName);
//        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,90);
//        desiredCapabilities.setCapability("nativeWebScreenshot","true");

        Map<String, Object> browserStackOptions = new HashMap<>();
        AppiumSettingsProperties appiumSettings = globalConfigProperties.getAppiumSettings();
        if(appiumSettings!=null){
            CapabilitiesProperties capabilitiesProperties = appiumSettings.getCapabilities();
            if(MapUtils.isNotEmpty(capabilitiesProperties.getDesiredCapabilitiesMap())){
                capabilitiesProperties.getDesiredCapabilitiesMap().forEach((key, value) -> browserStackOptions.put(key, value));
            }
        }
        desiredCapabilities.setCapability("bstack:options", browserStackOptions);
        return desiredCapabilities;
    }

    private static String getURLBrowserStack(AppiumSettingsProperties appiumSettings) throws WebEngineException{
        if(appiumSettings!=null){
            if(appiumSettings.getGridConnection().contains("browserstack.com")){
                return "https://"+appiumSettings.getUserName()+":"+appiumSettings.getPassword()+"@hub-cloud.browserstack.com/wd/hub";
            }else {
                return appiumSettings.getGridConnection();
            }
        }
        throw  new WebEngineException("Appium Settings are null. Check your application-properties.yml or your custom config ");
    }
}
