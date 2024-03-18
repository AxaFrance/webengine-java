package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.constante.IncognitoBrowserOption;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.Browser;
import fr.axa.automation.webengine.global.BrowserDetail;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.helper.BrowserTypeHelper;
import fr.axa.automation.webengine.helper.PlatformTypeHelper;
import fr.axa.automation.webengine.properties.AppiumCapabilities;
import fr.axa.automation.webengine.properties.AppiumConfiguration;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
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

    public static Optional<WebDriver> getDriver(GlobalConfiguration globalConfiguration) throws WebEngineException {
        return getDriver(globalConfiguration,true);
    }

    public static Optional<WebDriver> getDriver(GlobalConfiguration globalConfiguration, boolean deleteCookie) throws WebEngineException {
        Platform platform = PlatformTypeHelper.getPlatform(globalConfiguration.getWebengineConfiguration().getPlatformName());
        if (platform == Platform.WINDOWS) {
            return getDesktopDriver(globalConfiguration, deleteCookie);
        } else if (platform == Platform.ANDROID || platform == Platform.IOS) {
            return getAppiumDriver(globalConfiguration);
        } else {
            throw new WebEngineException("Not recognized the 'platform' parameter.");
        }
    }

    public static Optional<WebDriver> getIncognitoDriver(GlobalConfiguration globalConfiguration) throws WebEngineException {
        Platform platform = PlatformTypeHelper.getPlatform(globalConfiguration.getWebengineConfiguration().getPlatformName());
        Browser browser = BrowserTypeHelper.getBrowser(globalConfiguration.getWebengineConfiguration().getBrowserName());
        if (platform == Platform.WINDOWS) {
            return getWebDriver(platform, browser, true, IncognitoBrowserOption.getIncognitoBrowserOption(browser).getOptions());
        } else if (platform == Platform.ANDROID || platform == Platform.IOS) {
           return getAppiumDriver(globalConfiguration);
        } else {
            throw new WebEngineException("Not recognized the 'platform' parameter.");
        }
    }

    public static Optional<WebDriver> getDesktopDriver(GlobalConfiguration globalConfiguration, boolean deleteCookie) throws WebEngineException {
        Platform platform = PlatformTypeHelper.getPlatform(globalConfiguration.getWebengineConfiguration().getPlatformName());
        Browser browser = BrowserTypeHelper.getBrowser(globalConfiguration.getWebengineConfiguration().getBrowserName());
        List<String> browserOptionList = globalConfiguration.getWebengineConfiguration().getBrowserOptionList();
        if(CollectionUtils.isEmpty(globalConfiguration.getWebengineConfiguration().getBrowserOptionList())){
            browserOptionList = Collections.emptyList();
        }
        return getWebDriver(platform, browser, deleteCookie, browserOptionList);
    }

    public static Optional<WebDriver> getWebDriver(String platform, String browser, boolean deleteCookie) throws WebEngineException {
        return getWebDriver(PlatformTypeHelper.getPlatform(platform), BrowserTypeHelper.getBrowser(browser),deleteCookie);
    }

    public static Optional<WebDriver> getWebDriver(Platform platform, Browser browser,boolean deleteCookie) throws WebEngineException {
        return getWebDriver(platform,browser, deleteCookie, Collections.emptyList());
    }

    public static Optional<WebDriver> getWebDriver(String platform, String browser, boolean deleteCookie, List<String> browserOptionList) throws WebEngineException {
        return getWebDriver(PlatformTypeHelper.getPlatform(platform), BrowserTypeHelper.getBrowser(browser), deleteCookie, browserOptionList);
    }

    public static Optional<WebDriver> getWebDriver(Platform platform, Browser browser, boolean deleteCookie, List<String> browserOptionList) throws WebEngineException {
        BrowserDetail browserDetail = BrowserDetail.builder()
                                                    .platform(platform)
                                                    .browser(browser)
                                                    .deleteCookie(deleteCookie)
                                                    .browserOptionList(browserOptionList).build();
        return getWebDriver(browserDetail);
    }

    public static Optional<WebDriver> getWebDriver(BrowserDetail browserDetail) throws WebEngineException {
        WebDriver webDriver = null;
        Browser browser = browserDetail.getBrowser();
        List<String> browserOptionList = browserDetail.getBrowserOptionList();
        if (browserDetail.getPlatform() == Platform.WINDOWS) {
            if (browser == Browser.CHROME) {
                webDriver = ChromeDriverUtil.getChromeDriver(browserOptionList);
            } else if (browser == Browser.CHROMIUM_EDGE) {
                webDriver = EdgeDriverUtil.getEdgeDriver(browserOptionList);
            } else if (browser == Browser.FIREFOX) {
                webDriver = FirefoxDriverUtil.getFirefoxDriver(browserOptionList);
            }else{
                throw new WebEngineException("Browser not recognized");
            }
            if(browserDetail.isDeleteCookie()) {
                webDriver.manage().deleteAllCookies();
            }
        }
        return Optional.ofNullable(webDriver);
    }


    public static <T extends WebDriver> Optional<T> getAppiumDriver(GlobalConfiguration globalConfiguration) throws WebEngineException {
        Platform platform = PlatformTypeHelper.getPlatform(globalConfiguration.getWebengineConfiguration().getPlatformName());
        try {
            AppiumConfiguration appiumSettings = globalConfiguration.getWebengineConfiguration().getAppiumConfiguration();
            if (appiumSettings != null) {
                if (platform == Platform.ANDROID) {
                    return (Optional<T>) Optional.of(new AndroidDriver(new URL(getURLBrowserStack(appiumSettings)), getAppiumOption(globalConfiguration)));
                } else if (platform == Platform.IOS) {
                    return (Optional<T>) Optional.of(new IOSDriver(new URL(getURLBrowserStack(appiumSettings)), getAppiumOption(globalConfiguration)));
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

    private static DesiredCapabilities getAppiumOption(GlobalConfiguration globalConfiguration) throws WebEngineException {
        Browser browser = BrowserTypeHelper.getBrowser(globalConfiguration.getWebengineConfiguration().getBrowserName());
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browser.getValue());

        Map<String, Object> browserStackOptions = new HashMap<>();
        AppiumConfiguration appiumSettings = globalConfiguration.getWebengineConfiguration().getAppiumConfiguration();
        if (appiumSettings != null) {
            AppiumCapabilities capabilitiesProperties = appiumSettings.getCapabilities();
            if (MapUtils.isNotEmpty(capabilitiesProperties.getDesiredCapabilitiesMap())) {
                browserStackOptions.putAll(capabilitiesProperties.getDesiredCapabilitiesMap());
            }
        }
        desiredCapabilities.setCapability("bstack:options", browserStackOptions);
        return desiredCapabilities;
    }

    private static String getURLBrowserStack(AppiumConfiguration appiumSettings) throws WebEngineException {
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
