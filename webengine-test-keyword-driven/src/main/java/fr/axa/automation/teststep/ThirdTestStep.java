package fr.axa.automation.teststep;

import fr.axa.automation.action.ThirdStepAction;
import fr.axa.automation.webengine.api.IAction;
import fr.axa.automation.webengine.api.ITestStep;

public class ThirdTestStep implements ITestStep {

    @Override
    public Class<? extends IAction> getAction() {
        return ThirdStepAction.class;
    }
}
