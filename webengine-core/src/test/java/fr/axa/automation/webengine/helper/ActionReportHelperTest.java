package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ActionReportHelperTest {
    public final static String LOGIN_ACTION = "LoginAction";
    public final static String VISUALISATION_ACTION = "VisualisationAction";

    @Test
    void testGetArrayOfActionReport() {
        List<ActionReportDetail> actionReportDetailList = getActionReportDetailList();
        List<ActionReport> actionReportList = ActionReportHelper.getActionReportList(actionReportDetailList);
        Assertions.assertArrayEquals(new String[]{LOGIN_ACTION, VISUALISATION_ACTION}, actionReportList.stream().map(ActionReport::getName).toArray());
    }

    private List<ActionReportDetail> getActionReportDetailList() {
        ActionReportDetail actionReportDetail1 = ActionReportDetail.builder().actionReport(ActionReportHelper.getActionReport(LOGIN_ACTION)).resultCheckPoint(true).build();
        ActionReportDetail actionReportDetail2 = ActionReportDetail.builder().actionReport(ActionReportHelper.getActionReport(VISUALISATION_ACTION)).resultCheckPoint(true).build();
        return Arrays.asList(actionReportDetail1,actionReportDetail2);
    }

    @Test
    void testGetActionReport() {
        ActionReport actionReport = ActionReportHelper.getActionReport(LOGIN_ACTION);
        Assertions.assertEquals(LOGIN_ACTION,actionReport.getName());
        Assertions.assertEquals(Result.NONE,actionReport.getResult());
    }
}