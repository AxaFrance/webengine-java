package fr.axa.automation.webengine.report;

import fr.axa.automation.webengine.context.SharedInformation;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.PropertiesHelperProvider;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.constante.ReportPathConstant;
import fr.axa.automation.webengine.report.helper.ScreenshotHelper;
import fr.axa.automation.webengine.report.helper.TestCaseMetricHelper;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.report.helper.frmk.WebengineHtmlReportHelper;
import fr.axa.automation.webengine.report.helper.frmk.WebengineXmlReportHelper;
import fr.axa.automation.webengine.report.helper.global.ReportHelper;
import fr.axa.automation.webengine.report.helper.junit.JunitReportHelper;
import fr.axa.automation.webengine.report.object.TestCaseMetric;
import fr.axa.automation.webengine.util.ActiveWindowScreenShotUtil;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.ImageUtil;
import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportGherkinHelper implements IReportGherkinHelper {

    TestSuiteReport testSuiteReport;
    Map<String,TestCaseReport> testCaseReportMap;
    Map<String,ActionReport> actionReportMap;

    public ReportGherkinHelper() {
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

    public void addTestCaseReport(String featureName,String testCaseName){
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName);
        String normalizeTestCaseName = StringUtil.getNormalizeString(new String[]{featureName,testCaseName},StringUtil.DOUBLE_TWO_POINTS);
        testCaseReportMap.put(normalizeTestCaseName,testCaseReport);
    }

    public void updateTestCaseReport(String featureName,String testCaseName,Result result){
        TestCaseReport testCaseReport = getTestCaseReport(featureName, testCaseName, StringUtil.DOUBLE_TWO_POINTS);
        testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        testCaseReport.setResult(result);
    }

    private TestCaseReport getTestCaseReport(String featureName, String testCaseName, String doubleTwoPoints) {
        String normalizeTestCaseName = StringUtil.getNormalizeString(new String[]{featureName, testCaseName}, doubleTwoPoints);
        return testCaseReportMap.get(normalizeTestCaseName);
    }

    public void addTestStepReport(String featureName,String testCaseName, String testStepName){
        ActionReport actionReport = ActionReportHelper.getActionReport(testStepName);
        String normalizeName = StringUtil.getNormalizeString(new String[]{featureName,testCaseName,testStepName}, StringUtil.DOUBLE_TWO_POINTS);
        actionReportMap.put(normalizeName,actionReport);
    }

    public void updateTestStepReport(ReportDetail reportDetail){
        TestCaseReport testCaseReport = getTestCaseReport(reportDetail.getFeatureName(), reportDetail.getTestCaseName(), StringUtil.DOUBLE_TWO_POINTS);
        ActionReport actionReport = getActionReport(reportDetail);
        byte[] screenshot = ImageUtil.getImage(ActiveWindowScreenShotUtil.getGeneratedCurrentDesktopImage());
        actionReport.getScreenshots().getScreenshotReports().add(ScreenshotHelper.getScreenshotReport(reportDetail.getStepName(),screenshot));
        testCaseReport.getActionReports().getActionReports().add(actionReport);
    }

    private ActionReport getActionReport(ReportDetail reportDetail) {
        String normalizeName = StringUtil.getNormalizeString(new String[]{reportDetail.getFeatureName(),reportDetail.getTestCaseName(),reportDetail.getStepName()}, StringUtil.DOUBLE_TWO_POINTS);
        ActionReport actionReport = actionReportMap.get(normalizeName);
        List<String> informationList = SharedInformation.INFORMATION.get(normalizeName);
        StringJoiner stringJoiner = new StringJoiner("\n").add(CollectionUtils.isNotEmpty(informationList) ? informationList.toString() : "");
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
        Optional<GlobalConfiguration> globalConfigProperties = PropertiesHelperProvider.getInstance().getDefaultGlobalConfiguration();
        if(globalConfigProperties.isPresent()){
            applicationName = globalConfigProperties.get().getWebengineConfiguration().getName();
        }

        TestCaseMetric testCaseMetric = TestCaseMetricHelper.getMetrics(testCaseReportMap.values());
        testSuiteReport.setEndTime(Calendar.getInstance());
        testSuiteReport.getTestResults().addAll(testCaseReportMap.values());
        testSuiteReport.setNumberOfTestcase(testCaseMetric.getNumberOfTestCase());
        testSuiteReport.setPassed(testCaseMetric.getNumberOfTestCasePassed());
        testSuiteReport.setFailed(testCaseMetric.getNumberOfTestCaseFailed());
        testSuiteReport.setIgnored(testCaseMetric.getNumberOfTestCaseIgnored());

        ReportHelper reportHelper =  new ReportHelper(new WebengineXmlReportHelper(new LoggerService()), new WebengineHtmlReportHelper(new LoggerService()), new JunitReportHelper(new LoggerService()),new LoggerService());
        reportHelper.generateReports(testSuiteReport,applicationName, FileUtil.getPathInTargetDirectory(ReportPathConstant.REPORT_DIRECTORY_NAME.getValue()));
    }
}
