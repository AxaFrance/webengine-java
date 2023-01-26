package fr.axa.automation;

import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public class SampleTest {
    @Test
    public void linearApproachTest() throws WebEngineException {
        String baseUrl = "http://webengine-test.azurewebsites.net/";
        Optional<WebDriver> optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome");
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            driver.findElement(By.id("btnButtonOk")).click();
            driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            driver.quit();
        }
    }
}
