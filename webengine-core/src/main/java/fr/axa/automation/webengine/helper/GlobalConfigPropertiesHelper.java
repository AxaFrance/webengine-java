package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Settings;
import fr.axa.automation.webengine.properties.ApplicationProperties;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;


import java.util.Optional;

public class GlobalConfigPropertiesHelper {

    public static GlobalConfigProperties getGlobalConfigProperties(Settings settings) throws WebEngineException {
        ApplicationProperties applicationProperties = getApplicationProperties(settings);
        Optional<GlobalConfigProperties> globalConfigProperties = Optional.of(GlobalConfigProperties.builder().build());
        globalConfigProperties.get().setApplication(applicationProperties);
        return globalConfigProperties.get();
    }

    public static ApplicationProperties getApplicationProperties(Settings settings) {
        return ApplicationProperties.builder().platformName(settings.getPlatform().name()).browserName(settings.getBrowser().name()).outputDir(settings.getLogDir()).build();
    }
}
