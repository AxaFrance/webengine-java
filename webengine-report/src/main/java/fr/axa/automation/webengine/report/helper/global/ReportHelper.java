package fr.axa.automation.webengine.report.helper.global;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.constante.ReportKey;
import fr.axa.automation.webengine.report.helper.frmk.IWebengineHtmlReportHelper;
import fr.axa.automation.webengine.report.helper.frmk.IWebengineXmlReportHelper;
import fr.axa.automation.webengine.report.helper.junit.IJunitReportHelper;
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

    public Map<ReportKey,String> generateAllReport(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws  WebEngineException {
        Map<ReportKey,String> path = new HashMap<>();
        loggerService.info("Start Generation of Junit and Webengine Report");
        String webEngineReport = webengineXmlReportHelper.buildXmlReport(testSuiteReport, outputPath);
        webengineHtmlReportHelper.buildHtmlReport(testSuiteReport, outputPath,webEngineReport);
        String JunitReport = junitReportHelper.generateJUnitReport(testSuiteReport, testSuiteName, outputPath);
        path.put(ReportKey.WEBENGINE_REPORT_KEY,webEngineReport);
        path.put(ReportKey.JUNIT_REPORT_KEY,JunitReport);
        return path;
    }
}