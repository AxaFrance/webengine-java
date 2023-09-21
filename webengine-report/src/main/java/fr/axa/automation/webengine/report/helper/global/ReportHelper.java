package fr.axa.automation.webengine.report.helper.global;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.constante.ReportPathKey;
import fr.axa.automation.webengine.report.helper.TestSuiteReportHelper;
import fr.axa.automation.webengine.report.helper.frmk.IWebengineHtmlReportHelper;
import fr.axa.automation.webengine.report.helper.frmk.IWebengineXmlReportHelper;
import fr.axa.automation.webengine.report.helper.junit.IJunitReportHelper;
import fr.axa.automation.webengine.util.ApplicationDesktop;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ReportHelper implements IReportHelper{

    final IWebengineXmlReportHelper webengineXmlReportHelper;

    final IWebengineHtmlReportHelper webengineHtmlReportHelper;

    final IJunitReportHelper junitReportHelper;
    final ILoggerService loggerService;

    @Autowired
    public ReportHelper(IWebengineXmlReportHelper webengineReportHelper, IWebengineHtmlReportHelper webengineHtmlReportHelper, IJunitReportHelper junitReportHelper, ILoggerService loggerService) {
        this.webengineXmlReportHelper = webengineReportHelper;
        this.webengineHtmlReportHelper = webengineHtmlReportHelper;
        this.junitReportHelper = junitReportHelper;
        this.loggerService = loggerService;
    }

    public Map<ReportPathKey,String> generateReports(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws  WebEngineException {
        TestSuiteReport testSuiteReportCopy = TestSuiteReportHelper.clone(testSuiteReport);
        Map<ReportPathKey,String> path = new HashMap<>();
        path.put(ReportPathKey.XML_REPORT_PATH_KEY,generateWebengineXmlReport(testSuiteReportCopy, outputPath));
        path.put(ReportPathKey.HTML_REPORT_PATH_KEY,generateWebengineHtmlReport(testSuiteReportCopy, outputPath,path.get(ReportPathKey.XML_REPORT_PATH_KEY)));
        path.put(ReportPathKey.JUNIT_REPORT_PATH_KEY,generateJunitReport(testSuiteReportCopy, testSuiteName, outputPath));
        return path;
    }

    public String generateWebengineXmlReport(TestSuiteReport testSuiteReport, String outputPath) throws  WebEngineException {
        Map<ReportPathKey,String> path = new HashMap<>();
        loggerService.info("Start generation Webengine Report");
        return webengineXmlReportHelper.buildXmlReport(testSuiteReport, outputPath);
    }

    public String generateJunitReport(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws  WebEngineException {
        Map<ReportPathKey,String> path = new HashMap<>();
        loggerService.info("Start Generation of Junit Report");
        return junitReportHelper.generateJUnitReport(testSuiteReport, testSuiteName, outputPath);
    }

    public String generateWebengineHtmlReport(TestSuiteReport testSuiteReport, String outputPath, String webEngineXmlReport) throws  WebEngineException {
        Map<ReportPathKey,String> path = new HashMap<>();
        loggerService.info("Start Generation of Webengine HTML Report");
        return webengineHtmlReportHelper.buildHtmlReport(testSuiteReport, outputPath,webEngineXmlReport);
    }

    public void openReport(String reportPath) throws WebEngineException {
        loggerService.info("Start Opening Report");
        ApplicationDesktop.openDefaultBrowser(reportPath);
    }
}