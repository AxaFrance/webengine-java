package fr.axa.automation.webengine.logger;

public class LoggerServiceProvider {

    private LoggerServiceProvider() {
    }

    private static class LoggerServiceHolder{
        private final static LoggerService instance = new LoggerService();
    }

    public static LoggerService getInstance(){
        return LoggerServiceHolder.instance;
    }
}
