package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.localtesting.ILocalTestingRunner;
import fr.axa.automation.webengine.logger.ILoggerService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestSuiteExecutor implements ITestSuiteExecutor {

    ITestCaseExecutor testCaseExecutor;
    ILocalTestingRunner localTestingRunner;
    
    ILoggerService loggerService;

    protected AbstractTestSuiteExecutor(ITestCaseExecutor testCaseExecutor, ILocalTestingRunner localTestingRunner, ILoggerService loggerService) {
        this.testCaseExecutor = testCaseExecutor;
        this.localTestingRunner = localTestingRunner;
        this.loggerService = loggerService;
    }

    public Object initialize(AbstractGlobalApplicationContext globalApplicationContext) {
        loggerService.info("Start Phase initialize test suite ");
        runLocalTesting(globalApplicationContext);
        loggerService.info("End Phase initialize ");
        return null;
    }

    private void runLocalTesting(AbstractGlobalApplicationContext globalApplicationContext) {
        localTestingRunner.startLocalTesting();
    }

    public void cleanUp(Object object) {
        loggerService.info("Start clean ");
        stopLocalTesting();
        loggerService.info("End clean ");
    }

    private void stopLocalTesting() {
        localTestingRunner.stopLocalTesting();
    }
}
