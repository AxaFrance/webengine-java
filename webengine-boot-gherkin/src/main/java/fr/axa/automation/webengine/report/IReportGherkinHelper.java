package fr.axa.automation.webengine.report;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.Result;

import java.net.UnknownHostException;
import java.util.StringJoiner;

public interface IReportGherkinHelper {
    void createReport() throws UnknownHostException;
    void closeReport() throws WebEngineException;
    void updateTestCaseReport(String testCaseName, Result result);
    void addTestCaseReport(String testCaseName);
    void addTestStepReport(String testCaseName, String testStepName);
    void updateTestStepReport(ReportDetail reportDetail);
    void setCurrentFeatureName(String currentFeatureName);
    void setCurrentScenarioName(String currentScenarioName);
    void setCurrentStepName(String currentStepName);
    void setInformation(StringJoiner information);
}
