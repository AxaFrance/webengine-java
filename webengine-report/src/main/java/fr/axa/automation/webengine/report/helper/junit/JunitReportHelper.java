package fr.axa.automation.webengine.report.helper.junit;

import fr.axa.automation.junit.generated.ObjectFactory;
import fr.axa.automation.junit.generated.Testsuite;
import fr.axa.automation.webengine.constant.FileExtensionConstant;
import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.helper.ReportFileNameHelper;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FileUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JunitReportHelper implements IJunitReportHelper {

    public static final String JUNIT_REPORT_NAME = "junit-report";

    final ILoggerService loggerService;

    @Autowired
    public JunitReportHelper(ILoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public String generateJUnitReport(TestSuiteReport testSuiteReport, String testSuiteName, String outputPath) throws WebEngineException {
        Testsuite testsuite = createJUnitTestSuite(testSuiteReport,testSuiteName);
        Path directoryPath = FileUtil.createDirectories(outputPath);
        String fileName = ReportFileNameHelper.getFileName(JUNIT_REPORT_NAME, FileExtensionConstant.XML);
        Path completePath = Paths.get(directoryPath.toString(),fileName);
        InputMarshallDTO inputMarshallDTO = getInputMarshallDTO(testsuite, completePath);
        String junitReportPath = FileUtil.saveAsXml(inputMarshallDTO);
        loggerService.info("Create Junit report in : "+junitReportPath);
        return junitReportPath;
    }

    private InputMarshallDTO getInputMarshallDTO(Testsuite testsuite, Path completePath) {
        return InputMarshallDTO.builder().fileDestinationPath(completePath.toAbsolutePath().toString())
                                        .objectToMarshall(testsuite)
                                        .upperCaseRootElement(false)
                                        .namespace("")
                                        .prefix("").build();
    }

    public Testsuite createJUnitTestSuite(TestSuiteReport testSuiteReport, String testSuiteName) {
        Testsuite testsuite = new ObjectFactory().createTestsuite();
        testsuite.setName(StringUtils.isEmpty(testSuiteName) ? testSuiteName : "WebEngine Test Suite");
        testsuite.setTimestamp(Calendar.getInstance());
        if(testSuiteReport.getEndTime()!=null && testSuiteReport.getStartTime()!=null){
            testsuite.setTime(BigDecimal.valueOf(DateUtil.getDiff(testSuiteReport.getStartTime(),testSuiteReport.getEndTime())));
        }
        testsuite.setHostname(testSuiteReport.getHostName());
        testsuite.setProperties(new Testsuite.Properties());
        testsuite.setSystemOut(StringUtils.isNotEmpty(testSuiteReport.getSystemOut()) ? testSuiteReport.getSystemOut() : "");
        testsuite.setSystemErr(StringUtils.isNotEmpty(testSuiteReport.getSystemError()) ? testSuiteReport.getSystemError() : "");
        testsuite.setErrors((int)(testSuiteReport.getTestResults().stream().filter(elt->elt.getResult()== Result.FAILED).count()));
        testsuite.setTests(testSuiteReport.getTestResults().size());
        testsuite.getTestcases().addAll(getTestcases(testSuiteReport));
        return testsuite;
    }

    @Override
    public List<Testsuite.Testcase> getTestcases(TestSuiteReport testSuiteReport) {
        List<Testsuite.Testcase> testcaseList = new ArrayList<>();
        for (TestCaseReport testCaseReport : testSuiteReport.getTestResults()) {
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
