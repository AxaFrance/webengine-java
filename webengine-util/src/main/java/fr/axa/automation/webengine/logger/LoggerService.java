package fr.axa.automation.webengine.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Slf4j
public class LoggerService implements ILoggerService{
    private static final String PREFIX = "";
    private static final String SUFFIX = "";

    public void info(String message) {
        StringJoiner joiner = new StringJoiner("", PREFIX, SUFFIX);
        joiner.add(message);
        log.info(joiner.toString());
    }

    public void error(String message,Throwable e) {
        StringJoiner joiner = new StringJoiner("", PREFIX, SUFFIX);
        joiner.add(message);
        log.error(joiner.toString(),e);
    }


    public void warn(String message) {
        StringJoiner joiner = new StringJoiner("", PREFIX, SUFFIX);
        joiner.add(message);
        log.warn(joiner.toString());
    }

    public void warn(String message,Exception e) {
        StringJoiner joiner = new StringJoiner("", PREFIX, SUFFIX);
        joiner.add(message);
        log.warn(joiner.toString(),e);
    }

}
