package fr.axa.automation.webengine.report.helper.frmk;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;

public interface IWebengineHtmlReportHelper {

    void buildHtmlReport(TestSuiteReport testSuiteReport, String outputPath, String xmlFileName) throws WebEngineException;
}
