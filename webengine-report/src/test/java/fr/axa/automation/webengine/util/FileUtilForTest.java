package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtilForTest {

    public static final ILoggerService loggerService = LoggerServiceProvider.getInstance();

    public static void displayContent(String fileName){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(loggerService::info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
