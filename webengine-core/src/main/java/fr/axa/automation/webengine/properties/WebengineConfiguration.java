package fr.axa.automation.webengine.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class WebengineConfiguration {
    String name;
    String platformName = null;
    String browserName = null;
    String browserVersion = null;
    List<String> browserOptionList;
    String outputDir;
    AppiumConfiguration appiumConfiguration;
}

