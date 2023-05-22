package fr.axa.automation.webengine.logger;

public interface ILoggerService {

    void info(String message);
    void error(String message,Throwable e);
    void warn(String message,Exception e);
    void warn(String message);
}
