package fr.axa.automation.action;

import fr.axa.automation.appmodels.WebEngineHomeTestPage;
import fr.axa.automation.parameter.IParameter;
import fr.axa.automation.webengine.core.AbstractActionWebBase;
import fr.axa.automation.webengine.helper.VariableHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeAction extends AbstractActionWebBase {

    WebEngineHomeTestPage webEngineHomeTestPage;

    public HomeAction() {
    }

    @Override
    public void doAction() throws Exception {
        webEngineHomeTestPage = new WebEngineHomeTestPage(getWebDriver());
        String url = getEnvironnementValueWithException(IParameter.URL);
        getWebDriver().get(url);
        webEngineHomeTestPage.getStartStep1Link().scrollIntoView();
        webEngineHomeTestPage.getStartStep1Link().focus();
        webEngineHomeTestPage.getStartStep1Link().click();
        screenShot();
        addInformation("Home page");
        setContextValue(VariableHelper.getVariable("HOME_PAGE","SUCCESS"));
    }

    @Override
    public boolean doCheckpoint() throws Exception {
        return true;
    }
}