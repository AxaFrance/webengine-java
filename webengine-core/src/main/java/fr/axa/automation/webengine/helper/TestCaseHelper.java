package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TestCaseHelper {

    public static Result checkErrorInTestStep(List<ActionReport> actionReportList){
        List<ActionReport> actionReportErrorList = actionReportList.stream().filter(actionReport -> (actionReport.getResult() == Result.FAILED || actionReport.getResult() == Result.CRITICAL_ERROR)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(actionReportErrorList) ? Result.FAILED : null;
    }
}
