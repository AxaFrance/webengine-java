package fr.axa.automation.webengine.report.helper.junit;

import fr.axa.automation.junit.generated.Testsuite;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;

import java.util.List;

public interface IJunitReportHelper {
    List<Testsuite.Testcase> getTestcases(TestSuiteReport testSuiteReport);
    Testsuite createJUnitTestSuite(TestSuiteReport testSuiteReport, String testSuiteName);
    String generateJUnitReport(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws WebEngineException;
}
