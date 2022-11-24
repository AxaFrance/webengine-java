package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
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
public class PropertiesUtil {

    ILoggerService loggerService;

    GlobalConfigProperties globalConfigProperties;
    Map<String, Object> propertyFileMap;
    public static final String APPLICATION_FILE_NAME = "application-properties.yml";
    public static final String APPLICATION_FILE_NAME_WITHOUT_POSTFIX = "application-properties";

    public PropertiesUtil() {
        this.loggerService = new LoggerService();
        this.propertyFileMap = new HashMap<>();
    }

    public <T>T loadPropertiesFile(String resourceOrPathAndFileName, Class<T> clazz) throws WebEngineException {
        if(propertyFileMap.get(resourceOrPathAndFileName) == null){
            Yaml yaml = getYaml(clazz);
            try (InputStream inputStream = getPropertiesFileByPathOrResource(resourceOrPathAndFileName)){
                if(inputStream!=null){
                    propertyFileMap.put(resourceOrPathAndFileName,yaml.load(inputStream));
                }else{
                    loggerService.info("No "+resourceOrPathAndFileName+" file found.");
                }
            } catch (Exception e) {
                throw new WebEngineException("Error during reading "+resourceOrPathAndFileName+" file", e);
            }
        }
        return (T) propertyFileMap.get(resourceOrPathAndFileName);
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

    public Optional<GlobalConfigProperties> getGlobalConfigPropertiesByName(String resourceNameOrPathAndFileName) throws WebEngineException {
        GlobalConfigProperties globalConfigProperties = loadPropertiesFile(resourceNameOrPathAndFileName,GlobalConfigProperties.class);
        Optional<GlobalConfigProperties> optionalGlobalConfigProperties = Optional.empty();
        if(globalConfigProperties!=null){
            optionalGlobalConfigProperties = Optional.of(globalConfigProperties);
        }
        return optionalGlobalConfigProperties;
    }

    public Optional<GlobalConfigProperties> getDefaultGlobalConfiguration() throws WebEngineException{
        return getGlobalConfigPropertiesByName(APPLICATION_FILE_NAME);
    }

    public Optional<GlobalConfigProperties> getGlobalConfiguration(List<String> propertiesFileList, String resourceNameOrPathAndFileName) throws WebEngineException{
        Optional<String> applicationPropertiesFile = ListUtil.findFirst(propertiesFileList,resourceNameOrPathAndFileName);
        if(applicationPropertiesFile.isPresent()){
            return getGlobalConfigPropertiesByName(applicationPropertiesFile.get());
        }
        return Optional.empty();
    }

    //--!!!!!Use by project like e-declaration, axa.fr..., be careful
    public <T> Optional<T> getPropertiesByClass(List<String> propertiesFileList, String fileName, Class<T> clazz) throws WebEngineException {
        Optional<String> applicationPropertiesFile = ListUtil.findFirst(propertiesFileList,fileName);
        if(applicationPropertiesFile.isPresent()){
            return Optional.of(loadPropertiesFile(applicationPropertiesFile.get(),clazz));
        }
        return Optional.empty();
    }
}
