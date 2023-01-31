package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.helper.BrowserTypeHelper;
import fr.axa.automation.webengine.helper.PlatformTypeHelper;
import fr.axa.automation.webengine.properties.AppiumSettingsProperties;
import fr.axa.automation.webengine.properties.CapabilitiesProperties;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class BrowserFactory {

    private BrowserFactory() {
    }

    public static Optional<WebDriver> getDriver(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Platform platform = PlatformTypeHelper.getPlatform(globalConfigProperties.getApplication().getPlatformName());
        if (platform == Platform.WINDOWS) {
            return getDesktopDriver(globalConfigProperties);
        } else if (platform == Platform.ANDROID || platform == Platform.IOS) {
            return getAppiumDriver(globalConfigProperties);
        } else {
            throw new WebEngineException("Not recognized the 'platform' parameter.");
        }
    }

    public static Optional<WebDriver> getDesktopDriver(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Platform platform = PlatformTypeHelper.getPlatform(globalConfigProperties.getApplication().getPlatformName());
        Browser browser = BrowserTypeHelper.getBrowser(globalConfigProperties.getApplication().getBrowserName());
        if(CollectionUtils.isEmpty(globalConfigProperties.getApplication().getBrowserOptionList())){
            return getWebDriver(platform, browser);
        }else{
            return getWebDriver(platform, browser,globalConfigProperties.getApplication().getBrowserOptionList());
        }
    }

    public static Optional<WebDriver> getWebDriver(String platform, String browser) throws WebEngineException {
        return getWebDriver(PlatformTypeHelper.getPlatform(platform), BrowserTypeHelper.getBrowser(browser));
    }

    public static Optional<WebDriver> getWebDriver(Platform platform, Browser browser) throws WebEngineException {
        return getWebDriver(platform,browser, Collections.emptyList());
    }

    public static Optional<WebDriver> getWebDriver(String platform, String browser, List<String> browserOptionList) throws WebEngineException {
        return getWebDriver(PlatformTypeHelper.getPlatform(platform), BrowserTypeHelper.getBrowser(browser),browserOptionList);
    }

    public static Optional<WebDriver> getWebDriver(Platform platform, Browser browser, List<String> browserOptionList) throws WebEngineException {
        WebDriver webDriver = null;
        if (platform == Platform.WINDOWS) {
            if (browser == Browser.CHROME) {
                webDriver = ChromeDriverUtil.getChromeDriver(browserOptionList);
            } else if (browser == Browser.CHROMIUM_EDGE) {
                webDriver = EdgeDriverUtil.getEdgeDriver(browserOptionList);
            } else if (browser == Browser.FIREFOX) {
                webDriver = FirefoxDriverUtil.getFirefoxDriver(browserOptionList);
            }else{
                throw new WebEngineException("Browser not recognized");
            }
            webDriver.manage().deleteAllCookies();
        }
        return Optional.ofNullable(webDriver);
    }


    public static <T extends WebDriver> Optional<T> getAppiumDriver(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Platform platform = PlatformTypeHelper.getPlatform(globalConfigProperties.getApplication().getPlatformName());
        try {
            AppiumSettingsProperties appiumSettings = globalConfigProperties.getAppiumSettings();
            if (appiumSettings != null) {
                if (platform == Platform.ANDROID) {
                    return (Optional<T>) Optional.of(new AndroidDriver(new URL(getURLBrowserStack(appiumSettings)), getAppiumOption(globalConfigProperties)));
                } else if (platform == Platform.IOS) {
                    return (Optional<T>) Optional.of(new IOSDriver(new URL(getURLBrowserStack(appiumSettings)), getAppiumOption(globalConfigProperties)));
                } else {
                    throw new WebEngineException("Platform not recognized for getting Appium driver");
                }
            } else {
                throw new WebEngineException("Appium settings are null. Need the 'application-properties.file' property file ");
            }
        } catch (MalformedURLException e) {
            throw new WebEngineException("Error during getting Appium driver", e);
        }
    }

    private static DesiredCapabilities getAppiumOption(GlobalConfigProperties globalConfigProperties) throws WebEngineException {
        Browser browser = BrowserTypeHelper.getBrowser(globalConfigProperties.getApplication().getBrowserName());
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browser.getValue());

        Map<String, Object> browserStackOptions = new HashMap<>();
        AppiumSettingsProperties appiumSettings = globalConfigProperties.getAppiumSettings();
        if (appiumSettings != null) {
            CapabilitiesProperties capabilitiesProperties = appiumSettings.getCapabilities();
            if (MapUtils.isNotEmpty(capabilitiesProperties.getDesiredCapabilitiesMap())) {
                browserStackOptions.putAll(capabilitiesProperties.getDesiredCapabilitiesMap());
            }
        }
        desiredCapabilities.setCapability("bstack:options", browserStackOptions);
        return desiredCapabilities;
    }

    private static String getURLBrowserStack(AppiumSettingsProperties appiumSettings) throws WebEngineException {
        if (appiumSettings != null) {
            if (appiumSettings.getGridConnection().contains("browserstack.com")) {
                return "https://" + appiumSettings.getUserName() + ":" + appiumSettings.getPassword() + "@" +appiumSettings.getGridConnection().split("(?i)https://")[1];
            } else {
                return appiumSettings.getGridConnection();
            }
        }
        throw new WebEngineException("Appium Settings are null. Check your application-properties.yml or your custom config ");
    }
}
