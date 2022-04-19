package fr.axa.automation.webengine.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;

@Service
@Slf4j
public class LoggerService {
    private static final String PREFIX = "";
    private static final String SUFFIX = "";

    public void info(String message) {
        StringJoiner joiner = new StringJoiner("", PREFIX, SUFFIX);
        joiner.add(message);
       log.info(joiner.toString());
    }

    public void error(String message,Exception e) {
        StringJoiner joiner = new StringJoiner("", PREFIX, SUFFIX);
        joiner.add(message);
        log.error(joiner.toString(),e);
    }

}
