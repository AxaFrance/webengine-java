package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.report.object.TestCaseMetric;
import fr.axa.automation.webengine.report.object.TestSuiteReportInformation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

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

}
