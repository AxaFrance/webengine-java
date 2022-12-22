package fr.axa.automation.webengine.report;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.*;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.PropertiesHelperProvider;
import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.junit.JunitReportHelper;
import fr.axa.automation.webengine.report.helper.global.ReportHelper;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.report.helper.frmk.WebengineReportHelper;
import fr.axa.automation.webengine.util.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportGherkinHelper implements IReportGherkinHelper {

    TestSuiteReport testSuiteReport;
    Map<String,TestCaseReport> testCaseReportMap;
    Map<String,ActionReport> actionReportMap;

    String currentFeatureName;
    String currentScenarioName;
    String currentStepName;
    StringJoiner information;


    private enum NameNormalizeKey{
        TEST_CASE_NAME_NORMALIZE,TEST_STEP_NAME_NORMALIZE,TEST_CASE_AND_TEST_STEP_NAME_NORMALIZE
    }

    private ReportGherkinHelper() {
    }

    private static class ReportHelperGherkinHolder{
        private static final ReportGherkinHelper instance = new ReportGherkinHelper();
    }

    public static ReportGherkinHelper getInstance(){
        return ReportHelperGherkinHolder.instance;
    }

    public void createReport() throws UnknownHostException {
        initTestSuiteReport();
        testCaseReportMap = new HashMap<>();
        actionReportMap = new HashMap<>();
        information = new StringJoiner("\n");
    }

    private void initTestSuiteReport() throws UnknownHostException {
        testSuiteReport = new TestSuiteReport();
        testSuiteReport.setHostName(InetAddress.getLocalHost().getHostName());
        testSuiteReport.setStartTime(Calendar.getInstance());
    }

    private Map<NameNormalizeKey,String> getNormalizeTestCaseName(String testCaseName){
        Map<NameNormalizeKey,String> normalizeNameMap = new EnumMap(NameNormalizeKey.class);
        String testCaseNameNormalize = StringUtil.removeSpecialCharacters(testCaseName);
        normalizeNameMap.put(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE,testCaseNameNormalize);
        return normalizeNameMap;
    }

    private Map<NameNormalizeKey,String> getNormalizeName(String testCaseName, String testStepName){
        Map<NameNormalizeKey,String> normalizeNameMap = new EnumMap(NameNormalizeKey.class);
        String testCaseNameNormalize = getNormalizeTestCaseName(testCaseName).get(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE);
        String testStepNameNormalize = StringUtil.removeSpecialCharacters(testStepName);
        String testCaseAndTestStepNameNormalize = new StringJoiner(":").add(testCaseNameNormalize).add(testStepNameNormalize).toString();
        normalizeNameMap.put(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE,testCaseNameNormalize);
        normalizeNameMap.put(NameNormalizeKey.TEST_STEP_NAME_NORMALIZE,testStepNameNormalize);
        normalizeNameMap.put(NameNormalizeKey.TEST_CASE_AND_TEST_STEP_NAME_NORMALIZE,testCaseAndTestStepNameNormalize);
        return normalizeNameMap;
    }

    public void addTestCaseReport(String testCaseName){
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName);
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

    public void updateTestStepReport(ReportDetail reportDetail){
        Map<NameNormalizeKey,String> normalizeNameMap = getNormalizeName(reportDetail.getTestCaseName(),reportDetail.getStepName());
        TestCaseReport testCaseReport = testCaseReportMap.get(normalizeNameMap.get(NameNormalizeKey.TEST_CASE_NAME_NORMALIZE));
        ActionReport actionReport = getActionReport(reportDetail);
        byte[] screenshot = ImageUtil.getImage(ActiveWindowScreenShotUtil.getGeneratedCurrentDesktopImage());
        actionReport.getScreenshots().getScreenshotReports().add(ScreenshotHelper.getScreenshotReport(reportDetail.getStepName(),screenshot));
        testCaseReport.getActionReports().getActionReports().add(actionReport);
    }


    private ActionReport getActionReport(ReportDetail reportDetail) {
        Map<NameNormalizeKey,String> normalizeNameMap = getNormalizeName(reportDetail.getTestCaseName(),reportDetail.getStepName());
        ActionReport actionReport = actionReportMap.get(normalizeNameMap.get(NameNormalizeKey.TEST_CASE_AND_TEST_STEP_NAME_NORMALIZE));
        StringJoiner stringJoiner = new StringJoiner("\n").add(information.toString());
        if(reportDetail.getThrowable()!=null){
            stringJoiner.add(ExceptionUtils.getStackTrace(reportDetail.getThrowable()));
        }
        actionReport.setLog(stringJoiner.toString());
        actionReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        actionReport.setResult(reportDetail.getResult());
        return actionReport;
    }

    public void closeReport() throws  WebEngineException {
        String applicationName = "application";
        Optional<GlobalConfigProperties> globalConfigProperties = PropertiesHelperProvider.getInstance().getDefaultGlobalConfiguration();
        if(globalConfigProperties.isPresent()){
            applicationName = globalConfigProperties.get().getApplication().getName();
        }
        testSuiteReport.setEndTime(Calendar.getInstance());
        testSuiteReport.getTestResults().addAll(testCaseReportMap.values());
        ReportHelper reportHelper =  new ReportHelper(new WebengineReportHelper(new LoggerService()),new JunitReportHelper(new LoggerService()),new LoggerService());
        reportHelper.generateAllReport(testSuiteReport,applicationName,FileUtil.getPathInTargetDirectory(FileUtil.RUN_RESULT_DIRECTORY));
    }
}
