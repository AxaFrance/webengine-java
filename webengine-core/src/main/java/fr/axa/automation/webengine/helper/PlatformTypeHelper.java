package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Platform;

import java.util.Arrays;
import java.util.List;

public class PlatformTypeHelper {

    public static Platform getPlatform(String platformFill) throws WebEngineException {
        List<Platform> platformList = Arrays.asList(Platform.values());
        for (Platform platform:platformList) {
            if(platform.getValue().equalsIgnoreCase(platformFill)){
                return platform;
            }
        }
        throw new WebEngineException("unrecognized Platform value");
    }

}
