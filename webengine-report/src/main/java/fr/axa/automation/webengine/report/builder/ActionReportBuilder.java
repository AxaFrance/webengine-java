package fr.axa.automation.webengine.report.builder;

import fr.axa.automation.webengine.generated.ArrayOfActionReport;
import fr.axa.automation.webengine.generated.ArrayOfScreenshotReport;
import fr.axa.automation.webengine.generated.ArrayOfVariable;
import fr.axa.automation.webengine.generated.Result;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Calendar;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class ActionReportBuilder {

    protected String name;

    protected Result result;

    protected Calendar startTime;

    protected Calendar endTime;

    protected ArrayOfVariable contextValues;

    protected ArrayOfActionReport subActionReports;

    protected ArrayOfScreenshotReport screenshots;

    protected String log;
}
