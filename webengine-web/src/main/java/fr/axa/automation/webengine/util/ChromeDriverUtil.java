package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;
import java.util.List;

public final class ChromeDriverUtil {

    private ChromeDriverUtil() {
    }

    public static WebDriver getChromeDriver()  {
        return getChromeDriver(Collections.emptyList());
    }

    public static WebDriver getChromeDriver(List<String> chromeOptionList) {
        WebDriverManager.chromedriver().setup();
        if(CollectionUtils.isNotEmpty(chromeOptionList)){
            ChromeOptions chromeOptions1 = new ChromeOptions();
            chromeOptions1.addArguments(chromeOptionList);
            return new ChromeDriver(chromeOptions1);
        }
        return new ChromeDriver();
    }
}
