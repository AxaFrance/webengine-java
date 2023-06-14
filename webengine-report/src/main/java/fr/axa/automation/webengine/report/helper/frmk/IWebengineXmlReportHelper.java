package fr.axa.automation.webengine.report.helper.frmk;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;

public interface IWebengineXmlReportHelper {
    String buildXmlReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException;
}
