package fr.axa.automation.feature.step;

import fr.axa.automation.webengine.helper.WebdriverHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hook {

    public static WebDriver webDriver;

    @Before
    public void setUp() throws Exception {
        System.out.println("This will run before the Scenario");
        webDriver = WebdriverHelper.initializeDriver();
    }

    @After
    public void afterScenario()  throws Exception {
        WebdriverHelper.quiDriver(webDriver);
    }
}