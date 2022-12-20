package fr.axa.automation.webengine.report.helper.global;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.report.constante.ReportPath;

import java.util.Map;

public interface IReportHelper {
    Map<ReportPath,String> generateAllReport(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws WebEngineException;

}
