package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;

import java.io.FileNotFoundException;
import java.io.InputStream;

public final class PropertiesUtil {

    private PropertiesUtil() {
    }

    private static final ILoggerService loggerService = new LoggerService();

    public static <T> T loadPropertiesFile(String pathfileName, Class<T> clazz) throws WebEngineException {
        try (InputStream inputStream = FileUtil.getInputStreamByPathOrResource(pathfileName)) {
            if (inputStream != null) {
                return (T) YamlUtil.loadYaml(clazz, inputStream);
            }
        }catch (FileNotFoundException fileNotFoundException){
            displayWarningMsg(pathfileName);
        }catch (Exception e) {
            throw new WebEngineException("Error during reading " + pathfileName + " file", e);
        }
        return null;
    }

    private static void displayWarningMsg(String pathfileName) {
        String msg = "[WARNING!!] No " + pathfileName + " file found in resource folder. Maybe you use another";
        loggerService.warn(msg,new FileNotFoundException(msg));
    }
}
