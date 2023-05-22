package fr.axa.automation.webengine.report.object;

import fr.axa.automation.webengine.generated.ActionReport;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ActionReportDetail {
    ActionReport actionReport;
    boolean resultCheckPoint;
}
