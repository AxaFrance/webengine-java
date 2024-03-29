package fr.axa.automation.webengine.global;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum Browser {
    INTERNET_EXPLORER("InternetExplorer"), FIREFOX("Firefox"), CHROME("Chrome"), CHROMIUM_EDGE("ChromiumEdge"), IOS_NATIVE("IOSNative"), ANDROID_NATIVE("AndroidNative"),  SAFARI("Safari");
    final String value;
    public static Browser getDefaultBrowser(){
        return CHROMIUM_EDGE;
    }
}
