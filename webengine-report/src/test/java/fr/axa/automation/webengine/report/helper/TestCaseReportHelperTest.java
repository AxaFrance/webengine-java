package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Calendar;

public class TestCaseReportHelperTest {

    @Test
    void createTestCaseReport() {
        String testCaseName = "Scenario 1";
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName);
        Assertions.assertEquals(testCaseName,testCaseReport.getTestName());
        Assertions.assertNotNull(testCaseReport.getActionReports());
    }

    @Test
    void testCreateTestCaseReport() {
        String testCaseName = "Scenario 1";
        LocalDateTime localDateTime = LocalDateTime.of(2022,12,15,12,00,00);
        Calendar calendar = DateUtil.localDateTimeToCalendar(localDateTime);
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName,localDateTime);
        Assertions.assertEquals(testCaseName,testCaseReport.getTestName());
        Assertions.assertEquals(calendar,testCaseReport.getStartTime());
        Assertions.assertNotNull(testCaseReport.getActionReports());
    }
}