package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.ListUtil;
import fr.axa.automation.webengine.util.PropertiesUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertiesHelper {

    ILoggerService loggerService;

    Map<String, Object> propertyFileMap;

    public static final String APPLICATION_FILE_NAME = "application.yml";
    public static final String APPLICATION_FILE_NAME_WITHOUT_POSTFIX = "application";

    public PropertiesHelper() {
        this.loggerService = new LoggerService();
        this.propertyFileMap = new HashMap<>();
    }

    public <T> Optional<T> getPropertiesByClass(List<String> propertiesFileList, String fileName, Class<T> clazz) throws WebEngineException {
        Optional<String> applicationPropertiesFile = ListUtil.findFirst(propertiesFileList,fileName);
        if(applicationPropertiesFile.isPresent()){
            T t = loadPropertiesFile(applicationPropertiesFile.get(),clazz);
            return t!=null ? Optional.of(t) : Optional.empty();
        }
        return Optional.empty();
    }

    public <T>T loadPropertiesFile(String pathfileName, Class<T> clazz) throws WebEngineException {
        if(propertyFileMap.get(pathfileName) == null){
            propertyFileMap.put(pathfileName, PropertiesUtil.loadPropertiesFile(pathfileName, clazz));
        }
        return (T) propertyFileMap.get(pathfileName);
    }


    public Optional<GlobalConfigProperties> getDefaultGlobalConfiguration() throws WebEngineException{
        return getGlobalConfigurationByName(APPLICATION_FILE_NAME);
    }

    //--!!!!!Use by project like e-declaration, axa.fr..., be careful
    public Optional<GlobalConfigProperties> getGlobalConfiguration(List<String> propertiesFileList, String resourceNameOrPathAndFileName) throws WebEngineException{
        Optional<String> applicationPropertiesFile = ListUtil.findFirst(propertiesFileList,resourceNameOrPathAndFileName);
        if(applicationPropertiesFile.isPresent()){
            return getGlobalConfigurationByName(applicationPropertiesFile.get());
        }
        return Optional.empty();
    }

    public Optional<GlobalConfigProperties> getGlobalConfigurationByName(String resourceNameOrPathAndFileName) throws WebEngineException {
        GlobalConfigProperties globalConfigProperties = loadPropertiesFile(resourceNameOrPathAndFileName,GlobalConfigProperties.class);
        if(globalConfigProperties!=null){
            return Optional.of(globalConfigProperties);
        }
        return Optional.empty();
    }
}
