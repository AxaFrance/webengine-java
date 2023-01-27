package fr.axa.automation.testcase;

import fr.axa.automation.teststep.*;
import fr.axa.automation.webengine.core.ITestCase;
import fr.axa.automation.webengine.core.ITestStep;

import java.util.Arrays;
import java.util.List;

public class FlowTestCase implements ITestCase {

    @Override
    public List<? extends ITestStep> getTestStepList() {
        return Arrays.asList(new HomeTestStep(), new FirstTestStep(), new SecondTestStep(), new ThirdTestStep());
    }
}
