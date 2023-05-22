package fr.axa.automation.webengine.general;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum BrowserType {
    INTERNET_EXPLORER("InternetExplorer"), FIREFOX("Firefox"), CHROME("Chrome"), CHROMIUM_EDGE("ChromiumEdge"), IOS_NATIVE("IOSNative"), ANDROID_NATIVE("AndroidNative"),  SAFARI("Safari");
    final String value;
}
