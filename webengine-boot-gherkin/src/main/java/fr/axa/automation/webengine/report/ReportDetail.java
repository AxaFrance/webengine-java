package fr.axa.automation.webengine.report;

import fr.axa.automation.webengine.generated.Result;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ReportDetail {
    String featureName;
    String testCaseName;
    String stepName;
    Result result;
    Throwable throwable;
}
