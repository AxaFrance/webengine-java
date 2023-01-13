package fr.axa.automation.webengine.properties;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties
@ConfigurationPropertiesScan
public class GlobalConfigProperties {
    ApplicationProperties application;
    AppiumSettingsProperties appiumSettings;

    public boolean isLocalTestingConfExist() {
        return this.getAppiumSettings() != null && this.getAppiumSettings().getLocalTesting() != null;
    }

    public String getPlateform(){
        return this != null && this.getApplication() != null ? this.getApplication().getPlatformName() : null;
    }

    public String getBrowser(){
        return this != null && this.getApplication() != null ? this.getApplication().getBrowserName() : null;
    }

    public String getOutputDir(){
        return this != null && this.getApplication() != null ? this.getApplication().getOutputDir() : null;
    }
}