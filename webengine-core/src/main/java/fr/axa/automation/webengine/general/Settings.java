package fr.axa.automation.webengine.general;


import fr.axa.automation.webengine.report.ReportSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Builder
public class Settings {
    BrowserType browserType;
    @Builder.Default Integer synchronzationTimeout = 20;
    String logDir;
    String logFileName;
    String appId;
    String appPackageName;
    String device;
    String osVersion;
    Platform platform;
    String username;
    String password;
    String gridServerUrl = "http://localhost:4723/wd/hub";
    ReportSettings reportSettings;

}
