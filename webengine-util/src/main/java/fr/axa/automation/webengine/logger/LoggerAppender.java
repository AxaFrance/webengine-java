package fr.axa.automation.webengine.logger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;

public class LoggerAppender {

    public static String getFileAppender(){
        File clientLogFile;
        FileAppender<?> fileAppender = null;
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (Logger logger : context.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
                Object enumElement = index.next();
                if (enumElement instanceof FileAppender) {
                    fileAppender=(FileAppender<?>)enumElement;
                }
            }
        }
        clientLogFile = fileAppender != null ? new File(fileAppender.getFile()) : null;
        return clientLogFile.getAbsolutePath();
    }
}
