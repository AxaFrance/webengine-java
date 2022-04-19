package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class FirefoxDriverUtil {

    public static WebDriver getFirefoxDriver() throws WebEngineException {
        WebDriverManager.firefoxdriver().setup();
        return new EdgeDriver();
    }

}
