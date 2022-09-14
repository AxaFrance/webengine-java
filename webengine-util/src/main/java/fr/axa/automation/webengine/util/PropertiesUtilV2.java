package fr.axa.automation.webengine.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertiesUtilV2 {
    LoggerService loggerService;
    GlobalConfigProperties globalConfigProperties;
    static final String APPLICATION_FILE_NAME = "application-properties.yml";

    public PropertiesUtilV2() {
        this.loggerService = new LoggerService();
    }

    private static class PropertiesUtilHolder{
        private final static PropertiesUtilV2 INSTANCE = new PropertiesUtilV2();
    }

    public static PropertiesUtilV2 getInstance(){
        return PropertiesUtilV2.PropertiesUtilHolder.INSTANCE;
    }

    protected File loadFile() throws WebEngineException {
        File file = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(APPLICATION_FILE_NAME);
        if(url!=null){
            file = new File(url.getFile());
        }
        return file;
    }

    protected void loadPropertiesFile() throws WebEngineException {
        if (globalConfigProperties == null) {
            File file = loadFile();
            if (file != null) {
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                try {
                    globalConfigProperties = objectMapper.readValue(file, GlobalConfigProperties.class);
                } catch (IOException e) {
                    throw new WebEngineException("Error during reading application-properties.yaml file", e);
                }
            }
        }
    }

    public Optional<GlobalConfigProperties> getGlobalConfiguration() throws WebEngineException{
        loadPropertiesFile();
        Optional<GlobalConfigProperties> optionalGlobalConfigProperties = Optional.empty();
        if(globalConfigProperties!=null){
            optionalGlobalConfigProperties = Optional.of(globalConfigProperties);
        }
        return optionalGlobalConfigProperties;
    }
}
