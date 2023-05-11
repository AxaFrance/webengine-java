package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.properties.WebengineConfiguration;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.SettingsForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GlobalConfigPropertiesHelperTest {

    @Test
    void getGlobalConfigProperties() throws WebEngineException {
        GlobalConfigProperties globalConfigProperties = GlobalConfigPropertiesHelper.getGlobalConfigProperties(SettingsForTest.getSettings());
        Assertions.assertNotNull(globalConfigProperties);
        WebengineConfiguration webengineConfiguration = globalConfigProperties.getWebengineConfiguration();
        Assertions.assertNotNull(webengineConfiguration);
        Assertions.assertEquals(SettingsForTest.getSettings().getPlatform(),PlatformTypeHelper.getPlatform(webengineConfiguration.getPlatformName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getBrowser(),BrowserTypeHelper.getBrowser(webengineConfiguration.getBrowserName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getOutputDir(), webengineConfiguration.getOutputDir());
    }

    @Test
    void getApplicationProperties()  throws WebEngineException {
        WebengineConfiguration webengineConfiguration = GlobalConfigPropertiesHelper.getApplicationProperties(SettingsForTest.getSettings());
        Assertions.assertEquals(SettingsForTest.getSettings().getPlatform(),PlatformTypeHelper.getPlatform(webengineConfiguration.getPlatformName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getBrowser(),BrowserTypeHelper.getBrowser(webengineConfiguration.getBrowserName()));
        Assertions.assertEquals(SettingsForTest.getSettings().getOutputDir(), webengineConfiguration.getOutputDir());
    }
}