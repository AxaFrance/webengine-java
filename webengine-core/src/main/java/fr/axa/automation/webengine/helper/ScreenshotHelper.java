package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ArrayOfScreenshotReport;
import fr.axa.automation.webengine.generated.ScreenshotReport;

import java.util.Collections;
import java.util.List;

public final class ScreenshotHelper {

    private ScreenshotHelper() {
    }

    public static ArrayOfScreenshotReport getArrayOfScreenshotReport(String name, byte[] dataInbase64){
        ScreenshotReport screenshotReport = getScreenshotReport(name, dataInbase64);
        List<ScreenshotReport> screenshotReportList = Collections.singletonList(screenshotReport);
        return getArrayOfScreenshotReport(screenshotReportList);
    }

    private static ArrayOfScreenshotReport getArrayOfScreenshotReport(List<ScreenshotReport> screenshotReportList) {
        ArrayOfScreenshotReport arrayOfScreenshotReport = new ArrayOfScreenshotReport();
        arrayOfScreenshotReport.getScreenshotReports().addAll(screenshotReportList);
        return arrayOfScreenshotReport;
    }

    public static ScreenshotReport getScreenshotReport(String name, byte[] dataInbase64) {
        ScreenshotReport screenshotReport = new ScreenshotReport();
        screenshotReport.setName(name);
        screenshotReport.setData(dataInbase64);
        return screenshotReport;
    }
}
