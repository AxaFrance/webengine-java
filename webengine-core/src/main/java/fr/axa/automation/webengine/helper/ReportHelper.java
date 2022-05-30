package fr.axa.automation.webengine.helper;

import fr.axa.automation.junit.generated.ObjectFactory;
import fr.axa.automation.junit.generated.Testsuite;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.FormatDate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ReportHelper {

    @Autowired
    LoggerService loggerService;

    public void generateAllReport(TestSuiteReport testSuiteReport, String testName, String outputPath) throws IOException, WebEngineException {
        generateReport(testSuiteReport,testName,outputPath);
        generateJUnitReport(testSuiteReport,testName,outputPath);
    }

    public void generateReport(TestSuiteReport testSuiteReport, String testName, String outputPath) throws IOException, WebEngineException {
        Path path = FileUtil.createDirectories(outputPath + testName );

        StringBuilder composeFilePath = new StringBuilder("DataDrivenTestSuite-"+testName);
        String fileName = composeFilePath.append("_")
                        .append(DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat()))
                        .append(".xml").toString();
        loggerService.info("Create report : "+path.toString()+"\\"+fileName);
        FileUtil.saveAsXML(path.toString(),fileName,testSuiteReport);
    }


    public String generateJUnitReport(TestSuiteReport testSuiteReport, String testName, String outputPath) throws IOException, WebEngineException {
        Testsuite testsuite = createJUnitTestSuite(testSuiteReport, testName);
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
        testsuite.getTestcase().addAll(testcaseList);

        Path path = FileUtil.createDirectories(outputPath + testName );
        StringBuilder composeFilePath = new StringBuilder("Junit-"+testName);
        String fileName = composeFilePath.append("_")
                .append(DateUtil.getDateTime(FormatDate.YYYYMMDD_HHMMSS.getFormat()))
                .append(".xml").toString();
        loggerService.info("Create Junit report : "+path.toString()+"\\"+fileName);
        FileUtil.saveAsXML(path.toString(),fileName,testsuite);

        return path.toString();
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
        testcase.setTime(BigDecimal.valueOf(DateUtil.getDiff(testSuiteReport.getStartTime(),testSuiteReport.getStartTime())));
        testcase.setClassname(testCaseReport.getTestName());
        return testcase;
    }

    private Testsuite createJUnitTestSuite(TestSuiteReport testSuiteReport, String testName) {
        Testsuite testsuite = new ObjectFactory().createTestsuite();
        testsuite.setName(StringUtils.isEmpty(testName) ? testName : "WebEngine Test Suite");
        testsuite.setTimestamp(Calendar.getInstance());
        testsuite.setTime(BigDecimal.valueOf(DateUtil.getDiff(testSuiteReport.getStartTime(),testSuiteReport.getStartTime())));
        testsuite.setHostname(testSuiteReport.getHostName());
        testsuite.setSystemOut(testSuiteReport.getSystemOut());
        testsuite.setSystemErr(testSuiteReport.getSystemError());
        testsuite.setErrors(Long.valueOf(testSuiteReport.getTestResult().stream().filter(elt->elt.getResult()== Result.FAILED).count()).intValue());
        testsuite.setTests(testSuiteReport.getTestResult().size());
        return testsuite;
    }
}
