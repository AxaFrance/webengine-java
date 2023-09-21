package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.Platform;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class PlatformTypeHelper {

    private PlatformTypeHelper() {
    }

    public static Platform getPlatform(String platformFill) throws WebEngineException {
        List<Platform> platformList = Arrays.asList(Platform.values());

        Platform platform = foundPlatform(getPlatformPredicateWithName(platformFill));
        if (platform != null) {
            return platform;
        }

        platform = foundPlatform(getPlatformPredicateWithValue(platformFill));
        if (platform != null) {
            return platform;
        }

        throw new WebEngineException("unrecognized Platform value. Possible values are : " + platformList.stream().map(b -> b.getValue()).collect(Collectors.toList()));
    }

    private static Predicate<Platform> getPlatformPredicateWithName(String platformFill) {
        return platform -> platform.name().equalsIgnoreCase(platformFill);
    }

    private static Predicate<Platform> getPlatformPredicateWithValue(String platformFill) {
        return platform -> platform.getValue().equalsIgnoreCase(platformFill);
    }

    private static Platform foundPlatform(Predicate predicate) {
        List<Platform> platformList = Arrays.asList(Platform.values());
        List<Platform> platformFoundList = (List<Platform>) platformList.stream().filter(predicate).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(platformFoundList)) {
            return platformFoundList.get(0);
        }
        return null;
    }
}
