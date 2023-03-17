package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.core.TestCaseAdditionalInformation;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestSuiteData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class GlobalApplicationContextDriveByExcel extends AbstractGlobalApplicationContext{

    @Builder
    public GlobalApplicationContextDriveByExcel(Settings settings, EnvironmentVariables environmentVariables, TestSuiteData testSuiteData, Map<String, TestCaseAdditionalInformation> testCaseAdditionnalInformationList) {
        super(settings);
    }
}
