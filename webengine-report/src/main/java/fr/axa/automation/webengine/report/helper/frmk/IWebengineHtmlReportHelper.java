package fr.axa.automation.webengine.report.helper.frmk;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;

public interface IWebengineHtmlReportHelper {

    String buildHtmlReport(TestSuiteReport testSuiteReport, String outputPath, String xmlFileName) throws WebEngineException;
}
