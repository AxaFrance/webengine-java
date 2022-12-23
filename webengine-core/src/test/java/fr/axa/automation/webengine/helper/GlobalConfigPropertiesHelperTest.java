package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.properties.ApplicationProperties;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.SettingsForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalConfigPropertiesHelperTest {

    @Test
    void getGlobalConfigProperties() {
        GlobalConfigProperties globalConfigProperties = GlobalConfigPropertiesHelper.getGlobalConfigProperties(SettingsForTest.getSettings());
        Assertions.assertNotNull(globalConfigProperties);
        ApplicationProperties applicationProperties = globalConfigProperties.getApplication();
        Assertions.assertNotNull(applicationProperties);
        Assertions.assertEquals(SettingsForTest.getSettings().getPlatform(),applicationProperties.getPlatform());
        Assertions.assertEquals(SettingsForTest.getSettings().getBrowser(),applicationProperties.getBrowser());
        Assertions.assertEquals(SettingsForTest.getSettings().getLogDir(),applicationProperties.getOutputDir());
    }

    @Test
    void getApplicationProperties() {
        ApplicationProperties applicationProperties = GlobalConfigPropertiesHelper.getApplicationProperties(SettingsForTest.getSettings());
        Assertions.assertEquals(SettingsForTest.getSettings().getPlatform(),applicationProperties.getPlatform());
        Assertions.assertEquals(SettingsForTest.getSettings().getBrowser(),applicationProperties.getBrowser());
        Assertions.assertEquals(SettingsForTest.getSettings().getLogDir(),applicationProperties.getOutputDir());
    }
}