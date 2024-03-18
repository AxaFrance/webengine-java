package fr.axa.automation.webengine.global;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class BrowserDetail {
    Platform platform;
    Browser browser;
    boolean deleteCookie;
    List<String> browserOptionList;
}
