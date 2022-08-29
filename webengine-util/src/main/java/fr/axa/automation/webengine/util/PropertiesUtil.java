package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertiesUtil {
    Map<String,Properties> propertyFileMap;

    private PropertiesUtil() {
        propertyFileMap = new HashMap<>();
    }

    private static class PropertiesUtilHolder{
        private final static PropertiesUtil INSTANCE = new PropertiesUtil();
    }

    public static PropertiesUtil getInstance(){
        return PropertiesUtilHolder.INSTANCE;
    }

    public void loadPropertiesFile(String resourceName) throws WebEngineException {
        if(propertyFileMap.get(resourceName) == null){
            try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(resourceName)) {
                if (input == null) {
                    throw new WebEngineException("unable to find the resource file : "+resourceName);
                }
                Properties properties = new Properties();
                properties.load(input);
                propertyFileMap.put(resourceName,properties);
            } catch (IOException ex) {
                throw new WebEngineException("Error when load properties file : "+resourceName,ex);
            }
        }
    }

    public Optional<String> getValue(String resourceName,String property) throws WebEngineException{
        loadPropertiesFile(resourceName);
        return Optional.of(propertyFileMap.get(resourceName).getProperty(property));
    }
}
