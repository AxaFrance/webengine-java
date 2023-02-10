package fr.axa.automation.webengine.report.object;


import fr.axa.automation.webengine.generated.EnvironmentVariables;
import fr.axa.automation.webengine.generated.TestCaseReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Calendar;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuiteReportInformation {
    EnvironmentVariables environmentVariables;
    List<TestCaseReport> testCaseReportList;
    Calendar startTime;
    String systemError;
}
