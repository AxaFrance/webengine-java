package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.general.Settings;

public class SettingsForTest {

    public static Settings getSettings(){
        return  Settings.builder().platform(Platform.WINDOWS).browser(Browser.CHROMIUM_EDGE).logDir(FileUtil.getPathInTargetDirectory(FileUtil.RUN_RESULT_DIRECTORY)).build();
    }


}
