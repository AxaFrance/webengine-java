package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.properties.WebengineConfiguration;
import fr.axa.automation.webengine.util.SettingsForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GlobalConfigurationHelperTest {

    @Test
    void getGlobalConfigProperties() throws WebEngineException {
        GlobalConfiguration globalConfiguration = GlobalConfigPropertiesHelper.getGlobalConfigProperties(SettingsForTest.getSettings());
        Assertions.assertNotNull(globalConfiguration);
        WebengineConfiguration webengineConfiguration = globalConfiguration.getWebengineConfiguration();
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