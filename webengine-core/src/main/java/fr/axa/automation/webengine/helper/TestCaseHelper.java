package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;

import java.util.List;

public class TestCaseHelper {

    public static Result checkErrorInTestStep(List<ActionReport> actionReportList){
        Result result = null;
        for (ActionReport actionReport : actionReportList) {
            if (actionReport.getResult() == Result.FAILED || actionReport.getResult() == Result.CRITICAL_ERROR) {
                result = Result.FAILED;
                break;
            }
        }
        return result;
    }
}
