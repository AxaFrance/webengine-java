package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.properties.ApplicationProperties;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.SettingsForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GlobalConfigPropertiesHelperTest {

    @Test
    void getGlobalConfigProperties() throws WebEngineException {
        GlobalConfigProperties globalConfigProperties = GlobalConfigPropertiesHelper.getGlobalConfigProperties(SettingsForTest.getSettings());
        Assertions.assertNotNull(globalConfigProperties);
        ApplicationProperties applicationProperties = globalConfigProperties.getApplication();
        Assertions.assertNotNull(applicationProperties);
        Assertions.assertEquals(SettingsForTest.getSettings().getPlatform(),PlatformTypeHelper.getPlatform(applicationProperties.getPlatformName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getBrowser(),BrowserTypeHelper.getBrowser(applicationProperties.getBrowserName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getLogDir(),applicationProperties.getOutputDir());
    }

    @Test
    void getApplicationProperties()  throws WebEngineException {
        ApplicationProperties applicationProperties = GlobalConfigPropertiesHelper.getApplicationProperties(SettingsForTest.getSettings());
        Assertions.assertEquals(SettingsForTest.getSettings().getPlatform(),PlatformTypeHelper.getPlatform(applicationProperties.getPlatformName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getBrowser(),BrowserTypeHelper.getBrowser(applicationProperties.getBrowserName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getLogDir(),applicationProperties.getOutputDir());
    }
}