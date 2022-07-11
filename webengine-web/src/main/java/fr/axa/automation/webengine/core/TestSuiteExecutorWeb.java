package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.logger.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestSuiteExecutorWeb extends AbstractTestSuiteExecutor {

    @Autowired
    public TestSuiteExecutorWeb(LoggerService loggerService, ITestCaseExecutor testCaseExecutor) {
        super(loggerService, testCaseExecutor);
    }
}
