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
import java.nio.file.Paths;
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
        path.put(ReportPath.JUNIT_REPORT,JunitReport);
        return path;
    }

    public String generateWebengineReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        Path directoryPath = FileUtil.createDirectories(outputPath);
        String fileName = getFileName(WEBENGINE_REPORT_NAME);
        Path completePath = Paths.get(directoryPath.toString(),fileName);
        String resultPath = FileUtil.saveAsXml(completePath,testSuiteReport, NAMESPACE_WEBENGINE_REPORT, NS);
        loggerService.info("Create webengine report in : "+resultPath);
        return resultPath;
    }

    public String generateJUnitReport(TestSuiteReport testSuiteReport, String testSuiteName,String outputPath) throws  WebEngineException {
        Testsuite testsuite = junitReportHelper.createJUnitTestSuite(testSuiteReport,testSuiteName);
        Path directoryPath = FileUtil.createDirectories(outputPath);
        String fileName = getFileName(JUNIT_REPORT_NAME);
        Path completePath = Paths.get(directoryPath.toString(),fileName);
        String resultPath = FileUtil.saveAsXml(completePath,testsuite);
        loggerService.info("Create Junit report in : "+resultPath);
        return resultPath;
    }

    private static String getFileName(String prefixe) {
        StringBuilder composeFilePath = new StringBuilder(prefixe);
        return composeFilePath.append("-").append(DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat())).append(".xml").toString();
    }
}