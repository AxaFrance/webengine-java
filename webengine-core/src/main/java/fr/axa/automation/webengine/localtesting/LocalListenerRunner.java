package fr.axa.automation.webengine.localtesting;

import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceDecorator;

public class LocalListenerRunner {

    LoggerService loggerService = LoggerServiceDecorator.getInstance();;
    LocalTestingUtil localTestingUtil = LocalTestingUtil.getInstance();;

    public LocalListenerRunner() {
        this.loggerService = new LoggerService();
    }

    private static class LocalListenerRunnerHolder{
        private final static LocalListenerRunner INSTANCE = new LocalListenerRunner();
    }

    public static LocalListenerRunner getInstance(){
        return LocalListenerRunner.LocalListenerRunnerHolder.INSTANCE;
    }

    public void started() {
        try {
            localTestingUtil.startLocalTesting(getApplicationFileName());
        } catch (Exception e) {
            loggerService.error("Error when start local testing",e);
        }
    }

    public String getApplicationFileName() {
        return LocalTestingUtil.APPLICATION_FILE_NAME;
    }

    public void finished() {
        try {
            localTestingUtil.stopLocalTesting();
        } catch (Exception e) {
            loggerService.error("Error when stop local testing",e);
        }
    }
}
