package fr.axa.automation.webengine.report.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseMetric {
    Integer numberOfTestCase;
    Integer numberOfTestCasePassed;
    Integer numberOfTestCaseFailed;
    Integer numberOfTestCaseIgnored;
}
