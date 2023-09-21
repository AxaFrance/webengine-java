package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FormatDate;

public final class ReportFileNameHelper {

    private ReportFileNameHelper() {
    }

    public static String getFileNameWithSuffix(String prefixe) {
        StringBuilder composeFilePath = new StringBuilder(prefixe);
        return composeFilePath.append("-").append(DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat())).toString();
    }

    public static String getFileName(String prefixe,String extension) {
        StringBuilder composeFilePath = new StringBuilder(getFileNameWithSuffix(prefixe));
        return composeFilePath.append(".").append(extension).toString();
    }

    public static String getDirectoryName(String prefixe) {
        StringBuilder composeFilePath = new StringBuilder(getFileNameWithSuffix(prefixe));
        return composeFilePath.toString();
    }
}
