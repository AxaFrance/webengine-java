package fr.axa.automation.webengine.logger;

import org.junit.jupiter.api.Test;

class LoggerServiceTest {

    @Test
    void info() {
        ILoggerService loggerService = LoggerServiceProvider.getInstance();
        loggerService.info("INFO");
    }

    @Test
    void error() {
        ILoggerService loggerService = LoggerServiceProvider.getInstance();
        loggerService.error("ERROR",new Throwable());
    }

    @Test
    void warnWithException() {
        ILoggerService loggerService = LoggerServiceProvider.getInstance();
        loggerService.warn("WARN",new Exception());
    }

    @Test
    void warn() {
        ILoggerService loggerService = LoggerServiceProvider.getInstance();
        loggerService.warn("WARN");
    }
}