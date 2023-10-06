package fr.axa.automation.action;

import fr.axa.automation.appmodels.WebEngineThirdStepPage;
import fr.axa.automation.parameter.IParameter;
import fr.axa.automation.webengine.core.AbstractActionWebBase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThirdStepAction extends AbstractActionWebBase {

    WebEngineThirdStepPage webEngineThirdStepPage;

    public ThirdStepAction() {
    }

    @Override
    public void doAction() throws Exception {
        String date = getParameterWithException(IParameter.DATE);
        String password = getParameterWithException(IParameter.PASSWORD);
        webEngineThirdStepPage = new WebEngineThirdStepPage(getWebDriver());
        webEngineThirdStepPage.dateInput.setValue(date);
        webEngineThirdStepPage.passwordInput.setValue(password);
        webEngineThirdStepPage.nextStep.click();
        getWebDriver().switchTo().alert().accept();
        screenShot();
        addInformation("Second step succeed");
    }

    @Override
    public boolean doCheckpoint() throws Exception {
        return true;
    }
}