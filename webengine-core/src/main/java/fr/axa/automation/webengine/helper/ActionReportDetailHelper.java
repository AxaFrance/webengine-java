package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.report.object.ActionReportDetail;

public final class ActionReportDetailHelper {

    private ActionReportDetailHelper() {
    }

    public static ActionReportDetail getActionReportDetail(ActionReport actionReport, boolean resultCheckPoint) {
        return ActionReportDetail.builder().actionReport(actionReport).resultCheckPoint(resultCheckPoint).build();
    }
}
