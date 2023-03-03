package fr.axa.automation.teststep;

import fr.axa.automation.action.SecondStepAction;
import fr.axa.automation.webengine.api.IAction;
import fr.axa.automation.webengine.api.ITestStep;

public class SecondTestStep implements ITestStep {

    @Override
    public Class<? extends IAction> getAction() {
        return SecondStepAction.class;
    }
}
