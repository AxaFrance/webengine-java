package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.util.BrowserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

class WebElementDescriptionTest {

    public static final String HTTP_WEBENGINE_TEST_AZUREWEBSITES_NET = "https://axafrance.github.io/webengine-dotnet/demo/Test.html";

    @Test
    public void sendKeysTest() throws Exception {
        String baseUrl = HTTP_WEBENGINE_TEST_AZUREWEBSITES_NET;
        Optional<WebDriver> optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome", Arrays.asList("--remote-allow-origins=*"));
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);

            WebElementDescription webElementDescription = WebElementDescription.builder().id("inputValue").build();
            webElementDescription.populateDriver(driver);
            webElementDescription.sendKeys("TEST");

            driver.quit();
        }
    }

    @Test
    public void findByCollectionElementTest() throws Exception {
        String baseUrl = HTTP_WEBENGINE_TEST_AZUREWEBSITES_NET;
        Optional<WebDriver> optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome", Arrays.asList("--remote-allow-origins=*"));
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);

            WebElementDescription webElementDescription = WebElementDescription.builder().build();
            webElementDescription.populateDriver(driver);
            Collection<WebElement> webElementCollection = webElementDescription.findElements(By.id("inputValue"));
            Assertions.assertEquals(1,webElementCollection.size());
            driver.quit();
        }
    }

    @Test
    public void findByElementTest() throws Exception {
        String baseUrl = HTTP_WEBENGINE_TEST_AZUREWEBSITES_NET;
        Optional<WebDriver> optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome", Arrays.asList("--remote-allow-origins=*"));
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);

            WebElementDescription webElementDescription = WebElementDescription.builder().build();
            webElementDescription.populateDriver(driver);
            WebElement webElement = webElementDescription.findElement(By.id("inputValue"));
            Assertions.assertNotNull(webElement);
            driver.quit();
        }
    }

    @Test
    public void getAttributeTest() throws Exception {
        String baseUrl = HTTP_WEBENGINE_TEST_AZUREWEBSITES_NET;
        Optional<WebDriver> optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome", Arrays.asList("--remote-allow-origins=*"));
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);

            WebElementDescription webElementDescription = WebElementDescription.builder().id("inputValue").build();
            webElementDescription.populateDriver(driver);
            Assertions.assertNotNull("inputValue",webElementDescription.getAttribute("id"));

            driver.quit();
        }
    }

    @Test
    public void isDisplayedTest() throws Exception {
        String baseUrl = HTTP_WEBENGINE_TEST_AZUREWEBSITES_NET;
        Optional<WebDriver> optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome", Arrays.asList("--remote-allow-origins=*"));
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);

            WebElementDescription webElementDescription = WebElementDescription.builder().id("inputValue").build();
            webElementDescription.populateDriver(driver);
            boolean isDisplay = webElementDescription.isDisplayed();
            Assertions.assertEquals(true,isDisplay);

            driver.quit();
        }
    }

    @Test
    public void isEnabledTest() throws Exception {
        String baseUrl = HTTP_WEBENGINE_TEST_AZUREWEBSITES_NET;
        Optional<WebDriver> optionalWebdriver =  BrowserFactory.getWebDriver("Windows", "Chrome", Arrays.asList("--remote-allow-origins=*"));
        if(optionalWebdriver.isPresent()){
            WebDriver driver = optionalWebdriver.get();
            driver.get(baseUrl);

            WebElementDescription webElementDescription = WebElementDescription.builder().id("inputValue").build();
            webElementDescription.populateDriver(driver);
            boolean isEnabled = webElementDescription.isEnabled();
            Assertions.assertEquals(true,isEnabled);
            driver.quit();
        }
    }
}