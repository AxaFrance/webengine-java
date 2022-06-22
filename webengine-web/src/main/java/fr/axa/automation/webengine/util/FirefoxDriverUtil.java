package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Optional;

public class FirefoxDriverUtil {

    public static Optional<WebDriver> getFirefoxDriver() throws WebEngineException {
        WebDriverManager.firefoxdriver().setup();
        return Optional.of(new FirefoxDriver());
    }

}
