package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
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
        Optional<GlobalConfiguration> globalConfigProperties = propertiesHelper.getPropertiesByClass(Arrays.asList("properties/" + fileName),fileName, GlobalConfiguration.class);
        Assertions.assertTrue(globalConfigProperties.isPresent());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getWebengineConfiguration().getPlatformName()));
    }

    @Test
    void testLoadPropertiesFile() throws WebEngineException {
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        String fileName = "application-windows-chromiumedge.yml";
        GlobalConfiguration globalConfiguration = propertiesHelper.loadPropertiesFile("properties/" + fileName, GlobalConfiguration.class);
        Assertions.assertNotNull(globalConfiguration);
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfiguration.getWebengineConfiguration().getPlatformName()));
    }

    @Test
    void testGetDefaultGlobalConfiguration() throws WebEngineException {
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        Optional<GlobalConfiguration> globalConfigProperties = propertiesHelper.getDefaultGlobalConfiguration();
        Assertions.assertNotNull(globalConfigProperties.get());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getWebengineConfiguration().getPlatformName()));
    }

    @Test
    void testGetGlobalConfiguration() throws WebEngineException {
        String fileName = "properties/application-windows-chromiumedge.yml";
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        Optional<GlobalConfiguration> globalConfigProperties = propertiesHelper.getGlobalConfiguration(Collections.singletonList(fileName),fileName);
        Assertions.assertNotNull(globalConfigProperties.get());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getWebengineConfiguration().getPlatformName()));
    }

    @Test
    void testGetGlobalConfigPropertiesByName() throws WebEngineException {
        String fileName = "properties/application-windows-chromiumedge.yml";
        PropertiesHelper propertiesHelper = PropertiesHelperProvider.getInstance();
        Optional<GlobalConfiguration> globalConfigProperties = propertiesHelper.getGlobalConfigurationByName(fileName);
        Assertions.assertNotNull(globalConfigProperties.get());
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform(globalConfigProperties.get().getWebengineConfiguration().getPlatformName()));
    }
}