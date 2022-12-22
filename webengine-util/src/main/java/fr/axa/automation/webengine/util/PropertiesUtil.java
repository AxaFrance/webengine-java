package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PropertiesUtil {

    private static final ILoggerService loggerService = new LoggerService();

    public static <T> T loadPropertiesFile(String pathfileName, Class<T> clazz) throws WebEngineException {
        try (InputStream inputStream = FileUtil.getInputStreamByPathOrResource(pathfileName)) {
            if (inputStream == null) {
                loggerService.info("No " + pathfileName + " file found.");
                throw new FileNotFoundException("No " + pathfileName + " file found.");
            } else {
                return (T) YamlUtil.loadYaml(clazz, inputStream);
            }
        } catch (Exception e) {
            throw new WebEngineException("Error during reading " + pathfileName + " file", e);
        }
    }
}
