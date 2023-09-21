package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.generated.ArrayOfScreenshotReport;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ScreenshotHelperTest {

    private static final String SCREENSHOT_NAME = "login";

    @Test
    void testGetArrayOfScreenshotReport() {
        ArrayOfScreenshotReport arrayOfScreenshotReport = ScreenshotHelper.getArrayOfScreenshotReport(SCREENSHOT_NAME,null);
        List<ScreenshotReport> screenshotReportList = arrayOfScreenshotReport.getScreenshotReports();
        ScreenshotReport screenshotReport = screenshotReportList.get(0);
        Assertions.assertEquals(1,screenshotReportList.size());
        Assertions.assertEquals(SCREENSHOT_NAME,screenshotReport.getName());
        Assertions.assertNull(screenshotReport.getData());

    }

    @Test
    void testGetScreenshotReport() {
        ScreenshotReport screenshotReport = ScreenshotHelper.getScreenshotReport(SCREENSHOT_NAME,null);
        Assertions.assertEquals(SCREENSHOT_NAME,screenshotReport.getName());
        Assertions.assertNull(screenshotReport.getData());
    }
}