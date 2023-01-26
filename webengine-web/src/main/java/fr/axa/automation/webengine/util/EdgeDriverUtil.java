package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.Optional;

public final class EdgeDriverUtil {

    private EdgeDriverUtil() {
    }

    public static Optional<WebDriver> getEdgeDriver() throws WebEngineException {
        WebDriverManager.edgedriver().setup();
        return Optional.of(new EdgeDriver());
    }
}
