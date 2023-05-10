package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.global.AbstractSettings;
import fr.axa.automation.webengine.properties.ApplicationProperties;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;

public final class GlobalConfigPropertiesHelper {

    private GlobalConfigPropertiesHelper() {
    }

    public static GlobalConfigProperties getGlobalConfigProperties(AbstractSettings settings) {
        ApplicationProperties applicationProperties = getApplicationProperties(settings);
        GlobalConfigProperties globalConfigProperties = GlobalConfigProperties.builder().build();
        globalConfigProperties.setApplication(applicationProperties);
        return globalConfigProperties;
    }

    public static ApplicationProperties getApplicationProperties(AbstractSettings settings) {
        return ApplicationProperties.builder().platformName(settings.getPlatform().name())
                                                .browserName(settings.getBrowser().name())
                                                .browserOptionList(settings.getBrowserOptionsList())
                                                .outputDir(settings.getOutputDir())
                                                .build();
    }
}
