package fr.axa.automation.teststep;

import fr.axa.automation.action.ThirdStepAction;
import fr.axa.automation.webengine.core.IAction;
import fr.axa.automation.webengine.core.ITestStep;

public class ThirdTestStep implements ITestStep {

    @Override
    public Class<? extends IAction> getAction() {
        return ThirdStepAction.class;
    }
}
