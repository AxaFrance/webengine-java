package fr.axa.automation.webengine.global;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractSettings {
    Platform platform;
    Browser browser;
    List<String> browserOptionsList;
    List<String> propertiesFileList;
    String outputDir;
    Map<String,String> values;
    boolean showReport;
    boolean closeBrowser;
}
