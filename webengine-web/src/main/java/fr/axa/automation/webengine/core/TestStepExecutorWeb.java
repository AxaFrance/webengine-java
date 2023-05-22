package fr.axa.automation.webengine.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestStepExecutorWeb extends AbstractTestStepExecutor {

    @Autowired
    public TestStepExecutorWeb(IActionExecutor actionExecutor) {
        super(actionExecutor);
    }
}
