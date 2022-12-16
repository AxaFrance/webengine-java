package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.junit.generated.Testsuite;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.constante.ReportPath;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.FormatDate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ReportHelper implements IReportHelper{

    public static final String WEBENGINE_REPORT_NAME = "webengine-report";
    public static final String JUNIT_REPORT_NAME = "junit-report";
    public static final String NAMESPACE_WEBENGINE_REPORT = "http://www.axa.fr/WebEngine/2022";
    public static final String NS = "ns";

    final IJunitReportHelper junitReportHelper;
    final ILoggerService loggerService;

    @Autowired
    public ReportHelper(IJunitReportHelper junitReportHelper,ILoggerService loggerService) {
        this.junitReportHelper = junitReportHelper;
        this.loggerService = loggerService;
    }

    public Map<ReportPath,String> generateAllReport(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws  WebEngineException {
        Map<ReportPath,String> path = new HashMap<>();
        String webEngineReport = generateWebengineReport(testSuiteReport, outputPath);
        String JunitReport = generateJUnitReport(testSuiteReport, testSuiteName, outputPath);
        path.put(ReportPath.WEBENGINE_REPORT,webEngineReport);
        path.put(ReportPath.JUNITREPORT,JunitReport);
        return path;
    }

    public String generateWebengineReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        Path path = FileUtil.createDirectories(outputPath);
        String fileName = getFileName(WEBENGINE_REPORT_NAME);
        String completePath = FileUtil.saveAsXml(path.toString(),fileName,testSuiteReport, NAMESPACE_WEBENGINE_REPORT, NS);
        loggerService.info("Create webengine report in : "+completePath);
        return completePath;
    }

    public String generateJUnitReport(TestSuiteReport testSuiteReport, String testSuiteName,String outputPath) throws  WebEngineException {
        Testsuite testsuite = junitReportHelper.createJUnitTestSuite(testSuiteReport,testSuiteName);
        Path path = FileUtil.createDirectories(outputPath);
        String fileName = getFileName(JUNIT_REPORT_NAME);
        String completePath = FileUtil.saveAsXml(path.toString(),fileName,testsuite);
        loggerService.info("Create Junit report in : "+completePath);
        return completePath;
    }

    private static String getFileName(String prefixe) {
        StringBuilder composeFilePath = new StringBuilder(prefixe);
        return composeFilePath.append("-").append(DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat())).append(".xml").toString();
    }
}