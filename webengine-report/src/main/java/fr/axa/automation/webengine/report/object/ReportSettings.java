package fr.axa.automation.webengine.report.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Builder
public class ReportSettings {
    Boolean junitReport;
    String junitReportPath;
    Boolean nunitReport;
    String nunitReportPath;
}
