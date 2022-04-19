package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ArrayOfScreenshotReport;
import fr.axa.automation.webengine.generated.ScreenshotReport;

import java.util.ArrayList;
import java.util.List;

public class ScreenshotHelper {

    public static ArrayOfScreenshotReport getArrayOfScreenshotReport(String name, String dataInbase64){
        ArrayOfScreenshotReport arrayOfScreenshotReport = new ArrayOfScreenshotReport();
        List<ScreenshotReport> screenshotReportList = new ArrayList<>();
        ScreenshotReport screenshotReport = getScreenshotReport(name, dataInbase64);
        screenshotReportList.add(screenshotReport);
        arrayOfScreenshotReport.getScreenshotReport().addAll(screenshotReportList);
        return arrayOfScreenshotReport;
    }

    public static ScreenshotReport getScreenshotReport(String name, String dataInbase64) {
        ScreenshotReport screenshotReport = new ScreenshotReport();
        screenshotReport.setName(name);
        screenshotReport.setData(dataInbase64.getBytes());
        return screenshotReport;
    }

}
