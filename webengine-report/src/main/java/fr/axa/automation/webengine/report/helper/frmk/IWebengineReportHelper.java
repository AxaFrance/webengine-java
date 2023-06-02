package fr.axa.automation.webengine.report.helper.frmk;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;

import java.io.IOException;

public interface IWebengineReportHelper {
    String generateWebengineXmlReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException;

    void generateWebengineHtmlReport(TestSuiteReport testSuiteReport, String outputPath, String xmlFileName) throws WebEngineException;

}
