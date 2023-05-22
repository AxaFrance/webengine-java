package fr.axa.automation.webengine.report.helper.global;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.constante.ReportPath;
import fr.axa.automation.webengine.report.helper.frmk.IWebengineReportHelper;
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

    final IWebengineReportHelper webengineReportHelper;

    final IJunitReportHelper junitReportHelper;
    final ILoggerService loggerService;

    @Autowired
    public ReportHelper(IWebengineReportHelper webengineReportHelper,IJunitReportHelper junitReportHelper,ILoggerService loggerService) {
        this.webengineReportHelper = webengineReportHelper;
        this.junitReportHelper = junitReportHelper;
        this.loggerService = loggerService;
    }

    public Map<ReportPath,String> generateAllReport(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws  WebEngineException {
        Map<ReportPath,String> path = new HashMap<>();
        loggerService.info("Start Generation of Junit and Webengine Report");
        String webEngineReport = webengineReportHelper.generateWebengineReport(testSuiteReport, outputPath);
        path.put(ReportPath.WEBENGINE_REPORT,webEngineReport);
        String JunitReport = junitReportHelper.generateJUnitReport(testSuiteReport, testSuiteName, outputPath);
        path.put(ReportPath.JUNIT_REPORT,JunitReport);
        return path;
    }
}