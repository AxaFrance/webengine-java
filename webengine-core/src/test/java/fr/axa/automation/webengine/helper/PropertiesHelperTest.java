package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

class PropertiesHelperTest {

    @Test
    void testGetPropertiesByClass() throws WebEngineException {
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        String fileName = "application-windows-chromiumedge.yml";
        Optional<GlobalConfigProperties> globalConfigProperties = propertiesHelper.getPropertiesByClass(Arrays.asList("properties/" + fileName),fileName, GlobalConfigProperties.class);
        Assertions.assertTrue(globalConfigProperties.isPresent());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getApplication().getPlatformName()));
    }

    @Test
    void testLoadPropertiesFile() throws WebEngineException {
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        String fileName = "application-windows-chromiumedge.yml";
        GlobalConfigProperties globalConfigProperties = propertiesHelper.loadPropertiesFile("properties/" + fileName, GlobalConfigProperties.class);
        Assertions.assertNotNull(globalConfigProperties);
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.getApplication().getPlatformName()));
    }

    @Test
    void testGetDefaultGlobalConfiguration() throws WebEngineException {
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        Optional<GlobalConfigProperties> globalConfigProperties = propertiesHelper.getDefaultGlobalConfiguration();
        Assertions.assertNotNull(globalConfigProperties.get());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getApplication().getPlatformName()));
    }

    @Test
    void testGetGlobalConfiguration() throws WebEngineException {
        String fileName = "properties/application-windows-chromiumedge.yml";
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        Optional<GlobalConfigProperties> globalConfigProperties = propertiesHelper.getGlobalConfiguration(Collections.singletonList(fileName),fileName);
        Assertions.assertNotNull(globalConfigProperties.get());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getApplication().getPlatformName()));
    }

    @Test
    void testGetGlobalConfigPropertiesByName() throws WebEngineException {
        String fileName = "properties/application-windows-chromiumedge.yml";
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        Optional<GlobalConfigProperties> globalConfigProperties = propertiesHelper.getGlobalConfigurationByName(fileName);
        Assertions.assertNotNull(globalConfigProperties.get());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getApplication().getPlatformName()));
    }
}