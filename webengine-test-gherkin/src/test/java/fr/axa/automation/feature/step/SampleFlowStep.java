package fr.axa.automation.feature.step;


import fr.axa.automation.feature.model.WebEngineFirstStepPage;
import fr.axa.automation.feature.model.WebEngineFourthStepPage;
import fr.axa.automation.feature.model.WebEngineHomeTestPage;
import fr.axa.automation.feature.model.WebEngineSecondStepPage;
import fr.axa.automation.feature.model.WebEngineThirdStepPage;
import fr.axa.automation.webengine.step.AbstractStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SampleFlowStep extends AbstractStep {

    WebDriver driver;
    WebEngineHomeTestPage webEngineHomeTestPage;

    WebEngineFirstStepPage webEngineFirstStepPage;

    WebEngineSecondStepPage webEngineSecondStepPage;

    WebEngineThirdStepPage webEngineThirdStepPage;

    WebEngineFourthStepPage webEngineFourthStepPage;

    public SampleFlowStep() throws Exception {
        driver = Hook.webDriver;
        webEngineHomeTestPage = new WebEngineHomeTestPage(driver);
        webEngineFirstStepPage = new  WebEngineFirstStepPage(driver);
        webEngineSecondStepPage = new  WebEngineSecondStepPage(driver);
        webEngineThirdStepPage = new  WebEngineThirdStepPage(driver);
        webEngineFourthStepPage = new WebEngineFourthStepPage(driver);
    }

    @Given("^I visit the test page \"([^\"]*)\" for running journey$")
    public void visitTheTestPage(String url) throws InterruptedException {
        addInformation("Open WebEngine test page");
        driver.get(url);
        getWebEngineHomeTestPage().sync(3); //Just for code coverage
        getWebEngineHomeTestPage().maximize();//Just for code coverage
    }

    @And("^I click on the link Start step 1$")
    public void chooseTheLanguage() throws Exception {
        addInformation("Click on the link start step 1");
        getWebEngineHomeTestPage().getStartStep1Link().click();
    }

    @And("^I choose the language with text \"([^\"]*)\"$")
    public void chooseTheLanguage(String language) throws Exception {
        addInformation("Choose the language");
        getWebEngineFirstStepPage().getLanguage().selectByText(language);
    }

    @And("^I want to buy a coffee$")
    public void seePopUpAndEnterText() throws Exception {
        getWebEngineFirstStepPage().getCoffeeRadio().click();
    }

    @And("^I click on the first next button$")
    public void clickFirstButtonOKInThePopup() throws Exception {
        getWebEngineFirstStepPage().getNextStep().click();
    }

    @And("^I write a comment like \"([^\"]*)\"$")
    public void iWroteAComment(String comment) throws Exception {
        getWebEngineSecondStepPage().getResultTextarea().setValue(comment);
    }

    @And("^I choose also horns monster feature$")
    public void iChooseAlsoHornsFeature() throws Exception {
        getWebEngineSecondStepPage().getHornsCheckbox().click();
    }

    @And("^I click on the second the next button$")
    public void clickSecondButtonOKInThePopup() throws Exception {
        getWebEngineSecondStepPage().getNextStep().click();
    }

    @And("^I enter a date \"([^\"]*)\"$")
    public void enterADate(String date) throws Exception {
        getWebEngineThirdStepPage().getDateInput().setValue(date);
    }

    @And("^I enter a password \"([^\"]*)\"$")
    public void enterAPassword(String password) throws Exception {
        getWebEngineThirdStepPage().getPasswordInput().setValue(password);
    }

    @And("^I click on the third the next button$")
    public void clickThirdButtonOKInThePopup() throws Exception {
        getWebEngineThirdStepPage().getNextStep().click();
    }

    @And("^I click on the OK button in the pop up after i'm done$")
    public void clickButtonOKInThePopup() throws Exception {
        driver.switchTo().alert().accept();
    }

    @Then("^I see the Done title$")
    public void seeDone() throws Exception {
        Assertions.assertEquals("DONE",getWebEngineFourthStepPage().getDoneTitle().getText());
    }
}
