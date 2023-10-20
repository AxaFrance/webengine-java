package fr.axa.automation.webengine.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Collections;
import java.util.List;

public final class FirefoxDriverUtil {

    private FirefoxDriverUtil() {
    }

    public static WebDriver getFirefoxDriver()  {
        return getFirefoxDriver(Collections.emptyList());
    }

    public static WebDriver getFirefoxDriver(List<String> firefoxOptionList)  {
        WebDriverManager.firefoxdriver().setup();
        if(CollectionUtils.isNotEmpty(firefoxOptionList)){
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments(firefoxOptionList);
            return new FirefoxDriver(firefoxOptions);
        }
        return new FirefoxDriver();
    }

    public static WebDriver getFirefoxDriver(String browserVersion, List<String> firefoxOptionList)  {
        if(StringUtils.isEmpty(browserVersion)){
            WebDriverManager.firefoxdriver().setup();
        }else {
            WebDriverManager.firefoxdriver().browserVersion(browserVersion).setup();
        }

        if(CollectionUtils.isNotEmpty(firefoxOptionList)){
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments(firefoxOptionList);
            return new FirefoxDriver(firefoxOptions);
        }
        return new FirefoxDriver();

    }

}
