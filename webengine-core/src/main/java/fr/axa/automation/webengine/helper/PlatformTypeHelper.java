package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Platform;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlatformTypeHelper {

    public static Platform getPlatform(String platformFill) throws WebEngineException {
        List<Platform> platformList = Arrays.asList(Platform.values());

        List<Platform> platformFoundList = platformList.stream().filter(platform -> platform.getValue().equalsIgnoreCase(platformFill)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(platformFoundList)){
            return platformFoundList.get(0);
        }
        throw new WebEngineException("unrecognized Platform value. Possible values are : "+platformList);
    }

}
