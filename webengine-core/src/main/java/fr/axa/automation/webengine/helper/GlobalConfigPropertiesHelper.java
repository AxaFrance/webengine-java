package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.global.AbstractSettings;
import fr.axa.automation.webengine.properties.WebengineConfiguration;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;

public final class GlobalConfigPropertiesHelper {

    private GlobalConfigPropertiesHelper() {
    }

    public static GlobalConfigProperties getGlobalConfigProperties(AbstractSettings settings) {
        WebengineConfiguration webengineConfiguration = getApplicationProperties(settings);
        GlobalConfigProperties globalConfigProperties = GlobalConfigProperties.builder().build();
        globalConfigProperties.setWebengineConfiguration(webengineConfiguration);
        return globalConfigProperties;
    }

    public static WebengineConfiguration getApplicationProperties(AbstractSettings settings) {
        return WebengineConfiguration.builder().platformName(settings.getPlatform().name())
                                                .browserName(settings.getBrowser().name())
                                                .browserOptionList(settings.getBrowserOptionsList())
                                                .outputDir(settings.getOutputDir())
                                                .build();
    }
}
