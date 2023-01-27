package fr.axa.automation.teststep;

import fr.axa.automation.action.FirstStepAction;
import fr.axa.automation.webengine.core.IAction;
import fr.axa.automation.webengine.core.ITestStep;

public class FirstTestStep implements ITestStep {
    @Override
    public Class<? extends IAction> getAction() {
        return FirstStepAction.class;
    }
}
