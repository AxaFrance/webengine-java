package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.context.ExecutionDetail;
import fr.axa.automation.webengine.util.ActiveWindowScreenShotUtil;
import fr.axa.automation.webengine.util.ImageUtil;
import fr.axa.automation.webengine.util.ListUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ScreenshotHelper {
    public static final Map<String, List<byte[]>> SCREENSHOT = new ConcurrentHashMap<>();

    public static void screenshot() {
        if(CollectionUtils.isNotEmpty(ExecutionDetail.STEP_IN_PROGRESS)){
            byte[] screenshot = ImageUtil.getImage(ActiveWindowScreenShotUtil.getGeneratedCurrentDesktopImage());
            Optional<String> optionalKey = ListUtil.getLastElement(ExecutionDetail.STEP_IN_PROGRESS);
            if(optionalKey.isPresent()){
                String key = optionalKey.get();
                SCREENSHOT.computeIfAbsent(key, k -> new ArrayList<>()).add(screenshot);
            }
        }
    }
}
