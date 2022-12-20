package fr.axa.automation.webengine.report.helper.frmk;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;

public interface IWebengineReportHelper {
    String generateWebengineReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException;
}
