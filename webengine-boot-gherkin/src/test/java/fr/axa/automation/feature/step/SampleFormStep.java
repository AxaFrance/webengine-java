package fr.axa.automation.feature.step;

import fr.axa.automation.feature.model.WebEngineHomeTestPage;
import fr.axa.automation.webengine.step.AbstractStep;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SampleFormStep extends AbstractStep {

    WebEngineHomeTestPage pageModel;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        pageModel = new WebEngineHomeTestPage(webDriver);
    }

    @After
    public void afterScenario() throws Exception {
        super.afterScenario();
    }

    @Given("^I visit the test page \"([^\"]*)\"$")
    public void visitTheTestPage(String url) {
        addInformation("Open WebEngine test page");
        getWebDriver().get(url);
    }

    @When("^I press on the OK button$")
    public void pressOKButton() throws Exception {
        addInformation("I press the OK button");
        getPageModel().getOkButton().click();
    }

    @And("^I see a pop up$")
    public void seePopUpAndEnterText() throws Exception {
        addInformation("I See a pop up and i enter a text");
        String textInAlert = getWebDriver().switchTo().alert().getText();
        Assertions.assertEquals("hello world!",textInAlert);
    }

    @And("^I click on the OK button in the pop up$")
    public void clickButtonOKInThePopup() throws Exception {
        getWebDriver().switchTo().alert().accept();
    }
}
