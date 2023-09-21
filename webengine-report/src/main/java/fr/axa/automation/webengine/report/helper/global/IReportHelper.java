package fr.axa.automation.webengine.report.helper.global;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.report.constante.ReportPathKey;

import java.util.Map;

public interface IReportHelper {
    Map<ReportPathKey,String> generateReports(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws WebEngineException;
    void openReport(String reportPath) throws WebEngineException;

}
