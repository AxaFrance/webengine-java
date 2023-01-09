package fr.axa.automation.feature.authentication.step;

import fr.axa.automation.feature.authentication.model.WebEngineFirstStepPage;
import fr.axa.automation.feature.authentication.model.WebEngineHomeTestPage;
import fr.axa.automation.feature.authentication.model.WebEngineSecondStepPage;
import fr.axa.automation.feature.authentication.model.WebEngineThirdStepPage;
import fr.axa.automation.webengine.step.AbstractStep;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class SampleFlowStep extends AbstractStep {

    WebEngineHomeTestPage webEngineHomeTestPage;

    WebEngineFirstStepPage webEngineFirstStepPage;

    WebEngineSecondStepPage webEngineSecondStepPage;

    WebEngineThirdStepPage webEngineThirdStepPage;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        webEngineHomeTestPage = new WebEngineHomeTestPage(webDriver);
        webEngineFirstStepPage = new  WebEngineFirstStepPage(webDriver);
        webEngineSecondStepPage = new  WebEngineSecondStepPage(webDriver);
        webEngineThirdStepPage = new  WebEngineThirdStepPage(webDriver);
    }

    @After
    public void afterScenario() throws Exception {
        super.afterScenario();
    }


    @Given("^I visit the test page \"([^\"]*)\" for running journey$")
    public void visitTheTestPage(String url) {
        addInformation("Open WebEngine test page");
        getWebDriver().get(url);
    }

    @And("^I click on the link Start step 1$")
    public void chooseTheLanguage() throws Exception {
        addInformation("Click on the link start step 1");
        getWebEngineHomeTestPage().getStartStep1Link().click();
    }

    @And("^I choose the language with value \"([^\"]*)\"$")
    public void chooseTheLanguage(String language) throws Exception {
        addInformation("Choose the language");
        getWebEngineFirstStepPage().getLanguage().selectByValue(language);
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
}
