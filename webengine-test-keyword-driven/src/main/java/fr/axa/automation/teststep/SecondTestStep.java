package fr.axa.automation.teststep;

import fr.axa.automation.action.SecondStepAction;
import fr.axa.automation.webengine.core.IAction;
import fr.axa.automation.webengine.core.ITestStep;

public class SecondTestStep implements ITestStep {

    @Override
    public Class<? extends IAction> getAction() {
        return SecondStepAction.class;
    }
}
