package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.properties.ApplicationProperties;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;

public class GlobalConfigPropertiesHelper {

    public static GlobalConfigProperties getGlobalConfigProperties(Settings settings) {
        ApplicationProperties applicationProperties = getApplicationProperties(settings);
        GlobalConfigProperties globalConfigProperties = GlobalConfigProperties.builder().build();
        globalConfigProperties.setApplication(applicationProperties);
        return globalConfigProperties;
    }

    public static ApplicationProperties getApplicationProperties(Settings settings) {
        return ApplicationProperties.builder().platformName(settings.getPlatform().name())
                                                .browserName(settings.getBrowser().name())
                                                .outputDir(settings.getLogDir())
                                                .build();
    }
}
