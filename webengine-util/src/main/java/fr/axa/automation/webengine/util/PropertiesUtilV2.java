package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertiesUtilV2 {
    LoggerService loggerService;
    GlobalConfigProperties globalConfigProperties;
    Map<String, Object> propertyFileMap;
    static final String APPLICATION_FILE_NAME = "application-properties.yml";

    public PropertiesUtilV2() {
        this.loggerService = new LoggerService();
        propertyFileMap = new HashMap<>();
    }

    private static class PropertiesUtilHolder{
        private final static PropertiesUtilV2 INSTANCE = new PropertiesUtilV2();
    }

    public static PropertiesUtilV2 getInstance(){
        return PropertiesUtilV2.PropertiesUtilHolder.INSTANCE;
    }

    protected void loadPropertiesFile(String resourceName) throws WebEngineException {
        if (globalConfigProperties == null) {
            try {
                Yaml yaml = new Yaml(new Constructor(GlobalConfigProperties.class));
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
                if(inputStream!=null){
                    globalConfigProperties = yaml.load(inputStream);
                }else{
                    loggerService.info("No application-properties.yml file found.");
                }
            } catch (Exception e) {
                throw new WebEngineException("Error during reading application-properties.yaml file", e);
            }
        }
    }

    public <T>T loadPropertiesFile(String resourceName, Class<T> clazz) throws WebEngineException {
        if(propertyFileMap.get(resourceName) == null){
            try {
                Yaml yaml = new Yaml(new Constructor(clazz));
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
                if(inputStream!=null){
                    propertyFileMap.put(resourceName,yaml.load(inputStream));
                }else{
                    loggerService.info("No "+resourceName+" file found.");
                }
            } catch (Exception e) {
                throw new WebEngineException("Error during reading "+resourceName+" file", e);
            }
        }
        return (T) propertyFileMap.get(resourceName);
    }

    public Optional<GlobalConfigProperties> getGlobalConfiguration() throws WebEngineException{
        return getGlobalConfigPropertiesByName(APPLICATION_FILE_NAME);
    }

    public Optional<GlobalConfigProperties> getGlobalConfigPropertiesByName(String name) throws WebEngineException {
        loadPropertiesFile(name);
        Optional<GlobalConfigProperties> optionalGlobalConfigProperties = Optional.empty();
        if(globalConfigProperties!=null){
            optionalGlobalConfigProperties = Optional.of(globalConfigProperties);
        }
        return optionalGlobalConfigProperties;
    }
}
