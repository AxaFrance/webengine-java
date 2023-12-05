package fr.axa.automation.webengine;


import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.util.BrowserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Optional;

public class SampleTest {

    private Optional<WebDriver> optionalWebdriver;

    @BeforeEach // setup()
    public void setup() throws Exception {
        optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome",true, Arrays.asList("--remote-allow-origins=*"));
    }

    @AfterEach
    void tearDown() {
        WebDriver driver = optionalWebdriver.get();
        driver.quit();
    }

    @Test
    public void identifyShadowElementWithOneLevelTest() throws Exception {
        String baseUrl = "http://watir.com/examples/shadow_dom.html";
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            WebElementDescription webElementDescription = WebElementDescription.builder().useDriver(driver).build();
            WebElement webElement = webElementDescription.getElementInShadowByXpath("//*[@id='shadow_content']");
            Assertions.assertNotNull(webElement);
        }
    }

    @Test
    public void identifyShadowElementWithIdTest() throws Exception {
        String baseUrl = "http://watir.com/examples/shadow_dom.html";
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            WebElementDescription webElementDescription = WebElementDescription.builder().useDriver(driver).id("shadow_content").shadowDom(true).build();
            WebElement webElement = webElementDescription.findElement();
            Assertions.assertNotNull(webElement);
        }
    }

    @Test
    public void identifyShadowElementWithClassNameTest() throws Exception {
        String baseUrl = "http://watir.com/examples/shadow_dom.html";
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            WebElementDescription webElementDescription = WebElementDescription.builder().useDriver(driver).className("wrapper").shadowDom(true).build();
            WebElement webElement = webElementDescription.findElement();
            Assertions.assertNotNull(webElement);
        }
    }

    @Test
    public void identifyShadowElementWithSecondLevelTest() throws Exception {
        String baseUrl = "http://watir.com/examples/shadow_dom.html";
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);
            WebElementDescription webElementDescription = WebElementDescription.builder().useDriver(driver).build();
            WebElement webElement = webElementDescription.getElementInShadowByXpath("//*[@id='nested_shadow_content']");
            Assertions.assertNotNull(webElement);
        }
    }

}
