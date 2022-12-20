package fr.axa.automation.webengine.general;

import fr.axa.automation.webengine.core.TestCaseAdditionalInformation;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.TestSuiteData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class GlobalApplicationContext {
    Settings settings;
    EnvironmentVariables environmentVariables;
    TestSuiteData testSuiteData;
    Map<String, TestCaseAdditionalInformation> testCaseAdditionnalInformationList;

    public List<TestData> getTestDataList(){
        return getTestSuiteData().getTestDatas();
    }
}
