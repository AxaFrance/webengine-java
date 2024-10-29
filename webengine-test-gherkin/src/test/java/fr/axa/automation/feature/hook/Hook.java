package fr.axa.automation.feature.hook;

import fr.axa.automation.webengine.helper.WebdriverHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hook {

    public static WebDriver webDriver;

    @Before
    public void setUp() throws Exception {
        //mvn test -DprofileConfigFile=recette
        System.out.println("This will run before the Scenario");
        String profileConfigFile = System.getProperty("profileConfigFile");
        webDriver = WebdriverHelper.initializeWebDriver(profileConfigFile);
    }

    @After
    public void afterScenario()  throws Exception {
        WebdriverHelper.quitWebDriver(webDriver);
    }
}