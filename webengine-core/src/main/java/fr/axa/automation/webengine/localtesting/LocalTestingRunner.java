package fr.axa.automation.webengine.localtesting;

import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceDecorator;
import fr.axa.automation.webengine.util.PropertiesUtilV2;

public class LocalTestingRunner {

    LoggerService loggerService;
    LocalTestingUtil localTestingUtil = LocalTestingUtil.getInstance();

    public LocalTestingRunner() {
        this.loggerService = new LoggerService();
    }

    private static class LocalListenerRunnerHolder{
        private final static LocalTestingRunner INSTANCE = new LocalTestingRunner();
    }

    public static LocalTestingRunner getInstance(){
        return LocalTestingRunner.LocalListenerRunnerHolder.INSTANCE;
    }

    public void started() {
        started(getDefaultApplicationFileName());
    }

    public void started(String fileName) {
        try {
            localTestingUtil.startLocalTesting(fileName);
        } catch (Exception e) {
            loggerService.error("Error when start local testing",e);
        }
    }

    public String getDefaultApplicationFileName() {
        return PropertiesUtilV2.APPLICATION_FILE_NAME;
    }

    public void finished() {
        try {
            localTestingUtil.stopLocalTesting();
        } catch (Exception e) {
            loggerService.error("Error when stop local testing",e);
        }
    }
}
