package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ImageReportHelper {

    private ImageReportHelper(){
    }

    public static List<ScreenshotReport> getScreenShotReport(TestSuiteReport testSuiteReport) throws WebEngineException {
        List<ScreenshotReport> screenshotReportList = new ArrayList<>();
        if(testSuiteReport!=null){
            List<TestCaseReport> testCaseReportList = getTestResults(testSuiteReport);
            testCaseReportList.stream().forEach(testCaseReport -> screenshotReportList.addAll(getScreenShotReport(testCaseReport)));
        }
        return screenshotReportList;
    }

    private static List<TestCaseReport> getTestResults(TestSuiteReport testSuiteReport) {
        return testSuiteReport.getTestResults();
    }

    private static List<ScreenshotReport> getScreenShotReport(TestCaseReport testCaseReport) {
        List<ScreenshotReport> screenshotReportList = new ArrayList<>();
        if(testCaseReport!=null && testCaseReport.getActionReports()!=null && CollectionUtils.isNotEmpty(testCaseReport.getActionReports().getActionReports())){
            testCaseReport.getActionReports().getActionReports().stream().forEach(actionReport -> screenshotReportList.addAll(getScreenShotReport(actionReport)));
        }
        return screenshotReportList;
    }

    private static List<ScreenshotReport> getScreenShotReport(ActionReport actionReport) {
        List<ScreenshotReport> screenshotReportList = new ArrayList<>();
        List<ScreenshotReport> screenshotReportListOfSubReport = new ArrayList<>();
        if(actionReport != null){
            if(actionReport.getScreenshots() != null && CollectionUtils.isNotEmpty(actionReport.getScreenshots().getScreenshotReports())){
                screenshotReportList = actionReport.getScreenshots().getScreenshotReports().stream().collect(Collectors.toList());
            }
            if(actionReport.getSubActionReports()!=null){
                actionReport.getSubActionReports().getActionReports().stream().forEach(at -> screenshotReportListOfSubReport.addAll(getScreenShotReport(at)));
            }
        }
        screenshotReportList.addAll(screenshotReportListOfSubReport);
        return screenshotReportList;
    }
}
