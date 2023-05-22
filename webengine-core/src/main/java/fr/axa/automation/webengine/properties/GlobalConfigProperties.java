package fr.axa.automation.webengine.properties;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties
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