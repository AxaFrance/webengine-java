package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.report.object.TestCaseMetric;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class TestCaseMetricHelper {

    private TestCaseMetricHelper() {
    }

    public static TestCaseMetric getMetrics(Collection<TestCaseReport> testCaseReportList){
        return TestCaseMetric.builder()
                .numberOfTestCase(testCaseReportList.size())
                .numberOfTestCasePassed(getNumberOfTestCaseByStatus(testCaseReportList,Result.PASSED))
                .numberOfTestCaseFailed(getNumberOfTestCaseByStatus(testCaseReportList,Result.FAILED))
                .numberOfTestCaseIgnored(getNumberOfTestCaseByStatus(testCaseReportList,Result.IGNORED))
                .build();

    }

    public static Integer getNumberOfTestCaseByStatus(Collection<TestCaseReport> testCaseReportList, Result result){
        return testCaseReportList.stream().filter(testCaseReport -> testCaseReport.getResult()== result).collect(Collectors.toList()).size();
    }






}
