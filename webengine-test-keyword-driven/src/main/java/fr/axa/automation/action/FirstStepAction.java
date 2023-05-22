package fr.axa.automation.action;

import fr.axa.automation.appmodels.WebEngineFirstStepPage;
import fr.axa.automation.parameter.IParameter;
import fr.axa.automation.webengine.core.AbstractActionWebBase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FirstStepAction extends AbstractActionWebBase {
    WebEngineFirstStepPage webEngineFirstStepPage;
    public FirstStepAction() {
    }

    @Override
    public void doAction() throws Exception {
        String language = getParameterWithException(IParameter.LANGUAGE);
        webEngineFirstStepPage = new WebEngineFirstStepPage(getWebDriver());
        webEngineFirstStepPage.getLanguage().highLight();
        webEngineFirstStepPage.getLanguage().selectByValue(language);
        webEngineFirstStepPage.getCoffeeRadio().scrollIntoViewAndclick();
        webEngineFirstStepPage.getNextStep().click();
        screenShot(webEngineFirstStepPage.getLanguage());
        addInformation("First step succeed");
    }

    @Override
    public boolean doCheckpoint() throws Exception {
        return true;
    }
}