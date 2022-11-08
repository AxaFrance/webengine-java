package fr.axa.automation.webengine.general;


import fr.axa.automation.webengine.report.object.ReportSettings;
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
    @Builder.Default Integer synchronzationTimeout = 20;
    String logDir;
    String logFileName;
    String appId;
    String appPackageName;
    String osVersion;
    String device;
    String gridServerUrl = "http://localhost:4723/wd/hub";
    String username;
    String password;
    ReportSettings reportSettings;
}
