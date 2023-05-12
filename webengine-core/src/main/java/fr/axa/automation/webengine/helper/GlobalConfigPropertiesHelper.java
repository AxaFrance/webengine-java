package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.global.AbstractSettings;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.properties.WebengineConfiguration;

public final class GlobalConfigPropertiesHelper {

    private GlobalConfigPropertiesHelper() {
    }

    public static GlobalConfiguration getGlobalConfigProperties(AbstractSettings settings) {
        WebengineConfiguration webengineConfiguration = getApplicationProperties(settings);
        GlobalConfiguration globalConfiguration = GlobalConfiguration.builder().build();
        globalConfiguration.setWebengineConfiguration(webengineConfiguration);
        return globalConfiguration;
    }

    public static WebengineConfiguration getApplicationProperties(AbstractSettings settings) {
        return WebengineConfiguration.builder().platformName(settings.getPlatform().name())
                                                .browserName(settings.getBrowser().name())
                                                .browserOptionList(settings.getBrowserOptionsList())
                                                .outputDir(settings.getOutputDir())
                                                .build();
    }
}
