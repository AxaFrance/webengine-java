package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FormatDate;

public final class ReportFileNameHelper {

    private ReportFileNameHelper() {
    }

    public static String getFileName(String prefixe) {
        StringBuilder composeFilePath = new StringBuilder(prefixe);
        return composeFilePath.append("-").append(DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat())).append(".xml").toString();
    }
}
