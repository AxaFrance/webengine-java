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
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertiesUtilV2 {
    LoggerService loggerService;
    GlobalConfigProperties globalConfigProperties;
    Map<String, Object> propertyFileMap;
    public static final String APPLICATION_FILE_NAME = "application-properties.yml";

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

//    protected void loadPropertiesFile(String resourceName) throws WebEngineException {
//        if (globalConfigProperties == null) {
//            try {
//                Yaml yaml = new Yaml(new Constructor(GlobalConfigProperties.class));
//                InputStream inputStream = getPropertiesFile(resourceName);
//                if(inputStream!=null){
//                    globalConfigProperties = yaml.load(inputStream);
//                }else{
//                    loggerService.info("No application-properties.yml file found.");
//                }
//            } catch (Exception e) {
//                throw new WebEngineException("Error during reading application-properties.yaml file", e);
//            }
//        }
//    }

    protected void loadPropertiesFile(String resourceName) throws WebEngineException {
        if (globalConfigProperties == null) {
            globalConfigProperties = loadPropertiesFile(resourceName,GlobalConfigProperties.class);
        }
    }

//    public <T>T loadPropertiesFile(String fileOrResource, Class<T> clazz) throws WebEngineException {
//        if(propertyFileMap.get(fileOrResource) == null){
//            try {
//                Yaml yaml = getYaml(clazz);
//                InputStream inputStream = getPropertiesFileByPathOrResource(fileOrResource);
//                if(inputStream!=null){
//                    propertyFileMap.put(fileOrResource,yaml.load(inputStream));
//                }else{
//                    loggerService.info("No "+fileOrResource+" file found.");
//                }
//            } catch (Exception e) {
//                throw new WebEngineException("Error during reading "+fileOrResource+" file", e);
//            }
//        }
//        return (T) propertyFileMap.get(fileOrResource);
//    }

    public <T>T loadPropertiesFile(String fileOrResource, Class<T> clazz) throws WebEngineException {
        if(propertyFileMap.get(fileOrResource) == null){
            Yaml yaml = getYaml(clazz);
            try (InputStream inputStream = getPropertiesFileByPathOrResource(fileOrResource)){
                if(inputStream!=null){
                    propertyFileMap.put(fileOrResource,yaml.load(inputStream));
                }else{
                    loggerService.info("No "+fileOrResource+" file found.");
                }
            } catch (Exception e) {
                throw new WebEngineException("Error during reading "+fileOrResource+" file", e);
            }
        }
        return (T) propertyFileMap.get(fileOrResource);
    }

    private <T> Yaml getYaml(Class<T> clazz) {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        return new Yaml(new Constructor(clazz),representer);
    }

    private InputStream getPropertiesFileByPathOrResource(String fileOrResource) throws IOException {
        try {
            return new FileInputStream(fileOrResource);
        } catch (FileNotFoundException fileNotFoundException) {
            return getPropertiesFileInResource(fileOrResource);
        }
    }

    private InputStream getPropertiesFileInResource(String resourceName) {
        return this.getClass().getClassLoader().getResourceAsStream(resourceName);
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

    public Optional<GlobalConfigProperties> getGlobalConfigProperties(List<String> propertiesFileList, String fileName) throws WebEngineException {
        Optional<String> applicationPropertiesFile = propertiesFileList.stream().filter(s->s.contains(fileName)).findFirst();
        if(applicationPropertiesFile.isPresent()){
            return Optional.of(loadPropertiesFile(applicationPropertiesFile.get(),GlobalConfigProperties.class));
        }
        return Optional.empty();
    }
}
