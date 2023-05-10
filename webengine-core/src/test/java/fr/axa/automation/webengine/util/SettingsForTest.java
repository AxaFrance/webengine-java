package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.global.Browser;
import fr.axa.automation.webengine.global.Platform;
import fr.axa.automation.webengine.global.Settings;

public class SettingsForTest {

    public static Settings getSettings(){
        return  Settings.builder().platform(Platform.WINDOWS).browser(Browser.CHROMIUM_EDGE).outputDir(FileUtil.getPathInTargetDirectory(FileUtil.RUN_RESULT_DIRECTORY)).build();
    }


}
