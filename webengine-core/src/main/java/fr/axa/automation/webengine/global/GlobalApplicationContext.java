package fr.axa.automation.webengine.global;

import fr.axa.automation.webengine.core.TestCaseAdditionalInformation;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.TestSuiteData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class GlobalApplicationContext extends AbstractGlobalApplicationContext{
    EnvironmentVariables environmentVariables;
    TestSuiteData testSuiteData;
    Map<String, TestCaseAdditionalInformation> testCaseAdditionnalInformationList;

    @Builder
    public GlobalApplicationContext(Settings settings, EnvironmentVariables environmentVariables, TestSuiteData testSuiteData, Map<String, TestCaseAdditionalInformation> testCaseAdditionnalInformationList) {
        super(settings);
        this.environmentVariables = environmentVariables;
        this.testSuiteData = testSuiteData;
        this.testCaseAdditionnalInformationList = testCaseAdditionnalInformationList;
    }

    public List<TestData> getTestDataList(){
        return getTestSuiteData().getTestDatas();
    }
}
