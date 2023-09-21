package fr.axa.automation.testcase;

import fr.axa.automation.teststep.HomeTestStep;
import fr.axa.automation.webengine.core.ITestCase;
import fr.axa.automation.webengine.core.ITestStep;

import java.util.Arrays;
import java.util.List;

public class SimpleTestCase implements ITestCase {

    @Override
    public List<? extends ITestStep> getTestStepList() {
        return Arrays.asList(new HomeTestStep());
    }
}
