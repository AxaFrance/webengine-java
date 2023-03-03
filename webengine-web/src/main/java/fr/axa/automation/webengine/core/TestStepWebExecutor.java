package fr.axa.automation.webengine.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestStepWebExecutor extends AbstractTestStepExecutor {

    @Autowired
    public TestStepWebExecutor(IActionExecutor actionExecutor) {
        super(actionExecutor);
    }
}
