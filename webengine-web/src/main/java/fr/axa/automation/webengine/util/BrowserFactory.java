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
        Optional<GlobalConfigProperties> globalConfigProperties = PropertiesUtilV2.getInstance().getGlobalConfiguration();
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
        Optional<WebDriver> webDriver = Optional.empty();
        if(platform == Platform.WINDOWS){
            if(browser== Browser.CHROME){
                webDriver = ChromeDriverUtil.getChromeDriver();
            }else if(browser== Browser.CHROMIUM_EDGE){
                webDriver = EdgeDriverUtil.getEdgeDriver();
            }else if(browser== Browser.FIREFOX){
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
                    return (Optional<T>) Optional.of(new AndroidDriver(new URL(appiumSettings.getGridConnection()), getAppiumOption(globalConfigProperties)));
                }else if(platform == Platform.IOS){
                    return (Optional<T>) Optional.of(new IOSDriver(new URL(appiumSettings.getGridConnection()), getAppiumOption(globalConfigProperties)));
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
        Platform platform = Platform.valueOf(globalConfigProperties.getApplication().getPlatformName());
        Browser browser = Browser.valueOf(globalConfigProperties.getApplication().getBrowserName());
        String automationName = platform == Platform.ANDROID ? "UiAutomator2" : "Safari";
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("appium:platformName",platform.getValue());
        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,automationName);
        desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME,browser.getValue());
        desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,90);
        desiredCapabilities.setCapability("nativeWebScreenshot","true");
        AppiumSettingsProperties appiumSettings = globalConfigProperties.getAppiumSettings();
        if(appiumSettings!=null){
            CapabilitiesProperties capabilitiesProperties = appiumSettings.getCapabilities();
            if(MapUtils.isNotEmpty(capabilitiesProperties.getDesiredCapabilitiesMap())){
                capabilitiesProperties.getDesiredCapabilitiesMap().entrySet().forEach(cap -> desiredCapabilities.setCapability(cap.getKey(),cap.getValue()));
                if(appiumSettings.getGridConnection().contains("browserstack.com")){
                    desiredCapabilities.setCapability("bstack:options", getBrowserStackOptions(appiumSettings,capabilitiesProperties));
                }
            }
        }

        return desiredCapabilities;
    }

    private static Map<String, String> getBrowserStackOptions(AppiumSettingsProperties appiumSettings,CapabilitiesProperties capabilitiesProperties) throws WebEngineException {
        Map<String, String> browserstackOptions = new HashMap();
        browserstackOptions.put("userName", appiumSettings.getUserName());
        browserstackOptions.put("accessKey", appiumSettings.getPassword());
//        browserstackOptions.put("appiumVersion", "1.22.0");
//        browserstackOptions.put("projectName", "Java Project");
//        browserstackOptions.put("buildName", "browserstack-build-1");
//        browserstackOptions.put("sessionName", "first_test");
        return browserstackOptions;
    }
}
