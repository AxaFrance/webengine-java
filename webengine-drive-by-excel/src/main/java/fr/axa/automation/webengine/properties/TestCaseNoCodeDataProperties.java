package fr.axa.automation.webengine.properties;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@PropertySource(value="${external.data.properties.file}", ignoreResourceNotFound = true)
@ConfigurationProperties
@ConfigurationPropertiesScan
public class TestCaseNoCodeDataProperties {
    Map<String,String> testCaseDataMap;

    public String getTestCaseData(String key){
        return this != null && this.getTestCaseDataMap() != null ? this.getTestCaseDataMap().get(key) : null;
    }
}