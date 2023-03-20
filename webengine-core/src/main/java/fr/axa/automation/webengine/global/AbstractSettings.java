package fr.axa.automation.webengine.global;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractSettings {
    List<String> propertiesFileList;
    Platform platform;
    Browser browser;
    List<String> browserOptionsList;
    @Builder.Default Integer synchronizationTimeout = 20;
    String logDir;
}
