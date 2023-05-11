package fr.axa.automation.webengine.properties;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    WebengineConfiguration webengineConfiguration;

    public boolean isLocalTestingConfExist() {
        return this.getWebengineConfiguration()!=null && this.getWebengineConfiguration().getAppiumSettings() != null && this.getWebengineConfiguration().getAppiumSettings().getLocalTesting() != null;
    }

    public String getPlateform(){
        return this != null && this.getWebengineConfiguration() != null ? this.getWebengineConfiguration().getPlatformName() : null;
    }

    public String getBrowser(){
        return this != null && this.getWebengineConfiguration() != null ? this.getWebengineConfiguration().getBrowserName() : null;
    }

    public String getOutputDir(){
        return this != null && this.getWebengineConfiguration() != null ? this.getWebengineConfiguration().getOutputDir() : null;
    }
}