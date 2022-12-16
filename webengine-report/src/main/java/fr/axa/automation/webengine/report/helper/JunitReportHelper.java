package fr.axa.automation.webengine.report.helper;

import fr.axa.automation.junit.generated.ObjectFactory;
import fr.axa.automation.junit.generated.Testsuite;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.util.DateUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JunitReportHelper implements IJunitReportHelper {

    public Testsuite createJUnitTestSuite(TestSuiteReport testSuiteReport, String testSuiteName) {
        Testsuite testsuite = new ObjectFactory().createTestsuite();
        testsuite.setName(StringUtils.isEmpty(testSuiteName) ? testSuiteName : "WebEngine Test Suite");
        testsuite.setTimestamp(Calendar.getInstance());
        if(testSuiteReport.getEndTime()!=null && testSuiteReport.getStartTime()!=null){
            testsuite.setTime(BigDecimal.valueOf(DateUtil.getDiff(testSuiteReport.getStartTime(),testSuiteReport.getEndTime())));
        }
        testsuite.setHostname(testSuiteReport.getHostName());
        testsuite.setSystemOut(testSuiteReport.getSystemOut());
        testsuite.setSystemErr(testSuiteReport.getSystemError());
        testsuite.setErrors(Long.valueOf(testSuiteReport.getTestResult().stream().filter(elt->elt.getResult()== Result.FAILED).count()).intValue());
        testsuite.setTests(testSuiteReport.getTestResult().size());
        testsuite.getTestcase().addAll(getTestcases(testSuiteReport));
        return testsuite;
    }

    @Override
    public List<Testsuite.Testcase> getTestcases(TestSuiteReport testSuiteReport) {
        List<Testsuite.Testcase> testcaseList = new ArrayList<>();
        for (TestCaseReport testCaseReport : testSuiteReport.getTestResult()) {
            Testsuite.Testcase testcase = createJunitTestCase(testSuiteReport, testCaseReport);
            if(testCaseReport.getResult() == Result.FAILED){
                testcase.setFailure(createTestCaseFailure(testCaseReport));
            } else if(testCaseReport.getResult()==Result.IGNORED){
                testcase.setSkipped(createTestCaseSkipped(testCaseReport));
            }else if (testCaseReport.getResult()==Result.CRITICAL_ERROR){
                testcase.setError(createTestCaseError(testCaseReport));
            }
            testcaseList.add(testcase);
        }
        return testcaseList;
    }

    private Testsuite.Testcase.Failure createTestCaseFailure(TestCaseReport testCaseReport) {
        Testsuite.Testcase.Failure failure = new Testsuite.Testcase.Failure();
        failure.setMessage(testCaseReport.getLog());
        failure.setType(Result.FAILED.name());
        return failure;
    }

    private Testsuite.Testcase.Skipped createTestCaseSkipped(TestCaseReport testCaseReport) {
        Testsuite.Testcase.Skipped skipped = new Testsuite.Testcase.Skipped();
        skipped.setMessage(Result.IGNORED.name());
        return skipped;
    }

    private Testsuite.Testcase.Error createTestCaseError(TestCaseReport testCaseReport) {
        Testsuite.Testcase.Error error = new Testsuite.Testcase.Error();
        error.setMessage(testCaseReport.getLog());
        error.setType(Result.CRITICAL_ERROR.name());
        return error;
    }

    private Testsuite.Testcase createJunitTestCase(TestSuiteReport testSuiteReport, TestCaseReport testCaseReport) {
        Testsuite.Testcase testcase = new Testsuite.Testcase();
        testcase.setName(testCaseReport.getTestName());
        if(testSuiteReport.getEndTime()!=null && testSuiteReport.getStartTime()!=null) {
            testcase.setTime(BigDecimal.valueOf(DateUtil.getDiff(testSuiteReport.getStartTime(),testSuiteReport.getEndTime() )));
        }
        testcase.setClassname(testCaseReport.getTestName());
        return testcase;
    }


}
