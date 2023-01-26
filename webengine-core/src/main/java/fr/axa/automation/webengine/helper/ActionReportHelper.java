package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.ArrayOfScreenshotReport;
import fr.axa.automation.webengine.generated.ArrayOfVariable;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public final class ActionReportHelper {

    private ActionReportHelper() {
    }

    public static List<ActionReport> getActionReportList(List<ActionReportDetail> actionReportDetailList){
        if(CollectionUtils.isNotEmpty(actionReportDetailList)){
            return actionReportDetailList.stream().map(actionReportDetail -> actionReportDetail.getActionReport()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static ActionReport getActionReport(String name) {
        ActionReport actionReport = new ActionReport();
        actionReport.setName(name);
        actionReport.setStartTime(Calendar.getInstance());
        actionReport.setContextValues(new ArrayOfVariable());
        actionReport.setScreenshots(new ArrayOfScreenshotReport());
        actionReport.setResult(Result.NONE);
        return actionReport;
    }
}
