package fr.axa.automation.webengine.global;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum Platform {
    WINDOWS("Windows"), ANDROID("Android"), IOS("iOS");
    final String value;

    public static Platform getDefaultPlatform(){
        return WINDOWS;
    }
}
