package fr.axa.automation.teststep;

import fr.axa.automation.action.HomeAction;
import fr.axa.automation.webengine.api.IAction;
import fr.axa.automation.webengine.api.ITestStep;

public class HomeTestStep implements ITestStep {
    @Override
    public Class<? extends IAction> getAction() {
        return HomeAction.class;
    }
}
