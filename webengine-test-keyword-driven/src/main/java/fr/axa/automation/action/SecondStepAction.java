package fr.axa.automation.action;

import fr.axa.automation.appmodels.WebEngineSecondStepPage;
import fr.axa.automation.parameter.IParameter;
import fr.axa.automation.webengine.core.AbstractActionWebBase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecondStepAction extends AbstractActionWebBase {

    WebEngineSecondStepPage webEngineSecondStepPage;

    public SecondStepAction() {
    }

    @Override
    public void doAction() throws Exception {
        String comment = getParameterWithException(IParameter.COMMENT);
        webEngineSecondStepPage = new WebEngineSecondStepPage(getWebDriver());
        webEngineSecondStepPage.resultTextarea.setValue(comment);
        webEngineSecondStepPage.hornsCheckbox.click();
        webEngineSecondStepPage.nextStep.click();
        screenShot();
        addInformation("Second step succeed");
    }

    @Override
    public boolean doCheckpoint() throws Exception {
        return true;
    }
}