package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Optional;

public class ChromeDriverUtil {

    public static Optional<WebDriver> getChromeDriver() throws WebEngineException {
        WebDriverManager.chromedriver().setup();
        return Optional.of(new ChromeDriver());
    }
}
