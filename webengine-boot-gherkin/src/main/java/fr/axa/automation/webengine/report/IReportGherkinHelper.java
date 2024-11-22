package fr.axa.automation.webengine.report;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.Result;

import java.net.UnknownHostException;

public interface IReportGherkinHelper {
    void createReport() throws UnknownHostException;
    void closeReport() throws WebEngineException;
    void createTestCaseReport(String featureName, String testCaseName);
    void updateTestCaseReport(String featureName,String testCaseName, Result result);
    void createTestStepReport(String featureName, String testCaseName, String testStepName);
    void updateTestStepReport(ReportDetail reportDetail);
}
