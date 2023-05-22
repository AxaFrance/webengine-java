package fr.axa.automation.teststep;

import fr.axa.automation.action.HomeAction;
import fr.axa.automation.webengine.core.IAction;
import fr.axa.automation.webengine.core.ITestStep;

public class HomeTestStep implements ITestStep {
    @Override
    public Class<? extends IAction> getAction() {
        return HomeAction.class;
    }
}
