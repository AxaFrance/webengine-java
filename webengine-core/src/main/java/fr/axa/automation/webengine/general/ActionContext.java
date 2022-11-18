package fr.axa.automation.webengine.general;

import fr.axa.automation.webengine.core.TestCaseAdditionalInformation;
import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ActionContext {
    String testCaseName;
    Object webDriver; //Driver
    EnvironmentVariables environmentVariables;
    TestData testCaseData;
    TestCaseAdditionalInformation testCaseAdditionalInformation;
    Settings settings;
}
