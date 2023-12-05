package fr.axa.automation;

import fr.axa.automation.model.Page;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.Optional;

public class SampleTest {

    private Optional<WebDriver> optionalWebdriver;

    @BeforeEach // setup()
    public void setup() throws Exception {
        optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome", true,Arrays.asList("--remote-allow-origins=*"));
    }

    @AfterEach
    void tearDown() {
        WebDriver driver = optionalWebdriver.get();
        driver.quit();
    }


    @Test
    public void linearApproachTest() throws WebEngineException {
        String baseUrl = "https://axafrance.github.io/webengine-dotnet/demo/Test.html";
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            driver.findElement(By.id("btnButtonOk")).click();
            driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
        }
    }

    @Test
    public void linearApproachWithIncognitoModeTest() throws WebEngineException {
        String baseUrl = "https://axafrance.github.io/webengine-dotnet/demo/Test.html";
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            driver.findElement(By.id("btnButtonOk")).click();
            driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
        }
    }

    @Test
    public void linearApproachWithPageModel() throws Exception {
        String baseUrl = "https://axafrance.github.io/webengine-dotnet/demo/Step1.html";
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            Page page = new Page(driver);
            page.language.selectByText("Français");
            page.coffeeRadio.click();
            page.nextStep.click();
        }
    }
}
