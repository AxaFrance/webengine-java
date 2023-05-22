package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.localtesting.ILocalTestingRunner;
import fr.axa.automation.webengine.logger.ILoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestSuiteExecutorWeb extends AbstractTestSuiteExecutor {
    @Autowired
    public TestSuiteExecutorWeb(ITestCaseExecutor testCaseExecutor, ILocalTestingRunner localTestingRunner, ILoggerService loggerService) {
        super(testCaseExecutor,localTestingRunner, loggerService);
    }
}
