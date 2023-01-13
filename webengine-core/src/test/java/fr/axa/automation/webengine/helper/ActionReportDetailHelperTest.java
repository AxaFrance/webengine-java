package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.report.object.ActionReportDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ActionReportDetailHelperTest {

    @Test
    void getActionReportDetail() {
        String action = "LoginAction";
        ActionReportDetail actionReportDetail = ActionReportDetailHelper.getActionReportDetail(ActionReportHelper.getActionReport(action),false);
        Assertions.assertEquals(action,actionReportDetail.getActionReport().getName());
        Assertions.assertFalse(actionReportDetail.isResultCheckPoint());
    }
}