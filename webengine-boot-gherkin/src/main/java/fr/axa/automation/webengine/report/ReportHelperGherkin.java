package fr.axa.automation.webengine.report;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.*;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.report.helper.ReportHelper;
import fr.axa.automation.webengine.util.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportHelperGherkin {

    TestSuiteReport testSuiteReport;
    Map<String,TestCaseReport> testCaseReportMap;
    Map<String,ActionReport> actionReportMap;

    private enum NameNormalizeKey{
        TEST_CASE_NAME_NORMALIZE,TEST_STEP_NAME_NORMALIZE,TEST_CASE_AND_TEST_STEP_NAME_NORMALIZE
    }

    private ReportHelperGherkin() {
    }

    private static class ReportHelperGherkinHolder{
        private final static ReportHelperGherkin instance = new ReportHelperGherkin();
    }

    public static ReportHelperGherkin getInstance(){
        return ReportHelperGherkinHolder.instance;
    }

    public void createReport() throws UnknownHostException {
        initTestSuiteReport();
        testCaseReportMap = new HashMap<>();
        actionReportMap = new HashMap<>();
    }

    private void initTestSuiteReport() throws UnknownHostException {
        testSuiteReport = new TestSuiteReport();
        testSuiteReport.setHostName(InetAddress.getLocalHost().getHostName());
        testSuiteReport.setStartTime(Calendar.getInstance());
    }

    public TestCaseReport createTestCaseReport(String testCaseName){
        TestCaseReport testCaseReport = new TestCaseReport();
        testCaseReport.setTestName(testCaseName);
        testCaseReport.setStartTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        testCaseReport.setActionReports(new ArrayOfActionReport());
        return testCaseReport;
    }

    private Map<NameNormalizeKey,String> getNormalizeTestCaseName(String testCaseName){
        Map<NameNormalizeKey,String> normalizeNameMap = new HashMap<>();
        String testCaseNameNormalize = StringUtil.removeSpecialCharacters(testCaseName);
        normalizeNameMap.put(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE,testCaseNameNormalize);
        return normalizeNameMap;
    }

    private Map<NameNormalizeKey,String> getNormalizeName(String testCaseName, String testStepName){
        Map<NameNormalizeKey,String> normalizeNameMap = new HashMap<>();
        String testCaseNameNormalize = getNormalizeTestCaseName(testCaseName).get(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE);
        String testStepNameNormalize = StringUtil.removeSpecialCharacters(testStepName);
        String testCaseAndTestStepNameNormalize = new StringJoiner(":").add(testCaseNameNormalize).add(testStepNameNormalize).toString();
        normalizeNameMap.put(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE,testCaseNameNormalize);
        normalizeNameMap.put(NameNormalizeKey.TEST_STEP_NAME_NORMALIZE,testStepNameNormalize);
        normalizeNameMap.put(NameNormalizeKey.TEST_CASE_AND_TEST_STEP_NAME_NORMALIZE,testCaseAndTestStepNameNormalize);
        return normalizeNameMap;
    }

    public void addTestCaseReport(String testCaseName){
        TestCaseReport testCaseReport = createTestCaseReport(testCaseName);
        Map<NameNormalizeKey,String> normalizeName = getNormalizeTestCaseName(testCaseName);
        testCaseReportMap.put(normalizeName.get(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE),testCaseReport);
    }

    public void updateTestCaseReport(String testCaseName,Result result){
        Map<NameNormalizeKey,String> normalizeName = getNormalizeTestCaseName(testCaseName);
        TestCaseReport testCaseReport = testCaseReportMap.get(normalizeName.get(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE));
        testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        testCaseReport.setResult(result);
    }

    public void addTestStepReport(String testCaseName, String testStepName){
        Map<NameNormalizeKey,String> normalizeNameMap = getNormalizeName(testCaseName,testStepName);
        ActionReport actionReport = ActionReportHelper.getActionReport(testStepName);
        actionReportMap.put(normalizeNameMap.get(NameNormalizeKey.TEST_CASE_AND_TEST_STEP_NAME_NORMALIZE),actionReport);
    }

    public void updateTestStepReport(String testCaseName, String testStepName, Result result){
        Map<NameNormalizeKey,String> normalizeNameMap = getNormalizeName(testCaseName,testStepName);
        TestCaseReport testCaseReport = testCaseReportMap.get(normalizeNameMap.get(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE));
        ActionReport actionReport = actionReportMap.get(normalizeNameMap.get(NameNormalizeKey.TEST_CASE_AND_TEST_STEP_NAME_NORMALIZE));
        actionReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        actionReport.setResult(result);
        byte[] screenshot = ImageUtil.getImage(ActiveWindowScreenShot.getGeneratedCurrentDesktopImage());
        actionReport.getScreenshots().getScreenshotReport().add(ScreenshotHelper.getScreenshotReport(testStepName,screenshot));
        testCaseReport.getActionReports().getActionReport().add(actionReport);
    }

    public void closeReport() throws IOException, WebEngineException {
        Optional<String> optionalApplicationName = PropertiesUtil.getInstance().getValue("application.properties","application.name");
        String applicationName = "application";
        if(optionalApplicationName.isPresent()){
            applicationName = optionalApplicationName.get();
        }
        testSuiteReport.setEndTime(Calendar.getInstance());
        testSuiteReport.getTestResult().addAll(testCaseReportMap.values());
        ReportHelper reportHelper =  new ReportHelper(new LoggerService());
        reportHelper.generateAllReport(testSuiteReport,applicationName,FileUtil.getDefaultRunResultDirectory());
    }
}
