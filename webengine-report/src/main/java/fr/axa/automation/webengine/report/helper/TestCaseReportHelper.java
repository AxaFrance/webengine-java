package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.generated.ArrayOfActionReport;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.util.DateUtil;

import java.time.LocalDateTime;

public class TestCaseReportHelper {

    public static TestCaseReport createTestCaseReport(String testCaseName){
        return createTestCaseReport(testCaseName,LocalDateTime.now());
    }

    public static TestCaseReport createTestCaseReport(String testCaseName, LocalDateTime localDateTime){
        TestCaseReport testCaseReport = new TestCaseReport();
        testCaseReport.setTestName(testCaseName);
        testCaseReport.setStartTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        testCaseReport.setActionReports(new ArrayOfActionReport());
        return testCaseReport;
    }

}
