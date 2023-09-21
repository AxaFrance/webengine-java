package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.ArrayOfActionReport;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.report.object.TestCaseMetric;
import fr.axa.automation.webengine.report.object.TestSuiteReportInformation;
import fr.axa.automation.webengine.util.SerializationUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public final class TestSuiteReportHelper {

    private TestSuiteReportHelper(){

    }

    public static TestSuiteReport getTestSuiteReport(TestSuiteReportInformation testSuiteReportInformation) throws UnknownHostException {
        TestCaseMetric testCaseMetric = TestCaseMetricHelper.getMetrics(testSuiteReportInformation.getTestCaseReportList());
        TestSuiteReport testSuiteReport = new TestSuiteReport();
        testSuiteReport.setHostName(InetAddress.getLocalHost().getHostName());
        testSuiteReport.setStartTime(testSuiteReportInformation.getStartTime());
        testSuiteReport.setEnvironmentVariables(testSuiteReportInformation.getEnvironmentVariables());
        testSuiteReport.getTestResults().addAll(testSuiteReportInformation.getTestCaseReportList());
        testSuiteReport.setEndTime(Calendar.getInstance());
        testSuiteReport.setNumberOfTestcase(testCaseMetric.getNumberOfTestCase());
        testSuiteReport.setPassed(testCaseMetric.getNumberOfTestCasePassed());
        testSuiteReport.setFailed(testCaseMetric.getNumberOfTestCaseFailed());
        testSuiteReport.setIgnored(testCaseMetric.getNumberOfTestCaseIgnored());
        testSuiteReport.setSystemError(testSuiteReportInformation.getSystemError());
        return testSuiteReport;
    }

    public static TestSuiteReport clone(TestSuiteReport testSuiteReport) throws WebEngineException{
        TestSuiteReport testSuiteReportCopy;
        try {
            testSuiteReportCopy = SerializationUtils.clone(testSuiteReport);
            List< TestCaseReport> testCaseReportList = testSuiteReportCopy.getTestResults();
            updateTestCaseReport(testCaseReportList);
            updateScreenshotReport(testSuiteReportCopy);
            TestCaseMetric testCaseMetric = TestCaseMetricHelper.getMetrics(testSuiteReportCopy.getTestResults());
            testSuiteReportCopy.setPassed(testCaseMetric.getNumberOfTestCasePassed());
            testSuiteReportCopy.setFailed(testCaseMetric.getNumberOfTestCaseFailed());
            testSuiteReportCopy.setIgnored(testCaseMetric.getNumberOfTestCaseIgnored());
        } catch (IOException e) {
            throw new WebEngineException("Error during copy of object TestSuiteReport",e);
        }
        return testSuiteReportCopy;
    }

    private static void updateScreenshotReport(TestSuiteReport testSuiteReportCopy) throws WebEngineException {
        List<ScreenshotReport> screenshotReportList = ImageReportHelper.getScreenShotReport(testSuiteReportCopy);
        if(CollectionUtils.isNotEmpty(screenshotReportList)){
            for (ScreenshotReport screenshotReport :screenshotReportList) {
                if(StringUtils.isEmpty(screenshotReport.getId())){
                    screenshotReport.setId(UUID.randomUUID().toString());
                }
            }
        }
    }

    private static void updateTestCaseReport(List<TestCaseReport> testCaseReportList) {
        if(CollectionUtils.isNotEmpty(testCaseReportList)){
            for (TestCaseReport testCaseReport : testCaseReportList) {
                if(StringUtils.isEmpty(testCaseReport.getId())){
                    testCaseReport.setId(UUID.randomUUID().toString());
                }
                updateArrayOfActionReport(testCaseReport);
            }
        }
    }

    private static void updateArrayOfActionReport(TestCaseReport testCaseReport) {
        updateActionReport(testCaseReport.getActionReports());
    }

    private static void updateActionReport(ArrayOfActionReport arrayOfActionReport) {
        if(arrayOfActionReport!=null && CollectionUtils.isNotEmpty(arrayOfActionReport.getActionReports())){
            for (ActionReport actionReport : arrayOfActionReport.getActionReports()) {
                if(StringUtils.isEmpty(actionReport.getId())){
                    actionReport.setId(UUID.randomUUID().toString());
                }
                updateActionReport(actionReport.getSubActionReports());
            }
        }
    }
}
