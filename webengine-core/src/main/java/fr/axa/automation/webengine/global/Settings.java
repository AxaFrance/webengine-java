package fr.axa.automation.webengine.global;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Builder
public class Settings {
    List<String> propertiesFileList;
    Platform platform;
    Browser browser;
    List<String> browserOptionsList;
    List<String> testCaseToRunList;
    @Builder.Default Integer synchronizationTimeout = 20;
    String logDir;

}
