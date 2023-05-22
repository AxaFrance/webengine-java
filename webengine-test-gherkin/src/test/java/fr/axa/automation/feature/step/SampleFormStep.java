package fr.axa.automation.feature.step;

import fr.axa.automation.feature.model.WebEngineHomeTestPage;
import fr.axa.automation.webengine.step.AbstractStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SampleFormStep extends AbstractStep {

    WebDriver driver;
    WebEngineHomeTestPage pageModel;

    public SampleFormStep() throws Exception {
        driver = Hook.webDriver;
        pageModel = new WebEngineHomeTestPage(driver);
    }


    @Given("^I visit the test page \"([^\"]*)\"$")
    public void visitTheTestPage(String url) {
        addInformation("Open WebEngine test page");
        driver.get(url);
    }

    @When("^I press on the OK button$")
    public void pressOKButton() throws Exception {
        addInformation("I press the OK button");
        getPageModel().getOkButton().click();
    }

    @And("^I see a pop up$")
    public void seePopUpAndEnterText() throws Exception {
        addInformation("I See a pop up and i enter a text");
        String textInAlert = driver.switchTo().alert().getText();
        Assertions.assertEquals("hello world!",textInAlert);
    }

    @And("^I click on the OK button in the pop up$")
    public void clickButtonOKInThePopup() throws Exception {
        driver.switchTo().alert().accept();
    }
}
