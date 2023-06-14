package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.properties.WebengineTestProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PropertiesUtilTest {

    @Test
    void testLoadPropertiesFile() throws WebEngineException {
        WebengineTestProperties webengineTestProperties = PropertiesUtil.loadPropertiesFile("yaml/application.yml", WebengineTestProperties.class);
        Assertions.assertEquals("webengine", webengineTestProperties.getWebengineConfiguration().getName());
    }

    @Test
    void testLoadPropertiesFileWithException() throws WebEngineException {
        WebengineTestProperties webengineTestProperties = PropertiesUtil.loadPropertiesFile("yaml/application-no-exist.yml", WebengineTestProperties.class);
        Assertions.assertEquals(null, webengineTestProperties);
    }
}