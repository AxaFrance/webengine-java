package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.report.ActionReportDetail;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ActionReportHelper {

    public static List<ActionReport> getArrayOfActionReport(List<ActionReportDetail> actionReportDetailList){
        List<ActionReport> actionReportList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(actionReportDetailList)){
            for (ActionReportDetail actionReportDetail:actionReportDetailList) {
                actionReportList.add(actionReportDetail.getActionReport());
            }
        }
        return actionReportList;
    }

}
