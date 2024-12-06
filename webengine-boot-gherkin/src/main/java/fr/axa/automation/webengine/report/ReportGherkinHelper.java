package fr.axa.automation.webengine.report;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.ArrayOfVariable;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.ArrayOfVariableHelper;
import fr.axa.automation.webengine.helper.NameNormalizerHelper;
import fr.axa.automation.webengine.helper.PropertiesHelperProvider;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.logger.Logger;
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
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FileUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportGherkinHelper implements IReportGherkinHelper {

    TestSuiteReport testSuiteReport;
    Map<String,TestCaseReport> testCaseReportMap;
    Map<String,ActionReport> actionReportMap;

    public ReportGherkinHelper() throws UnknownHostException {
        createReport();
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

    public void createTestCaseReport(String featureName, String testCaseName){
        String normalizeTestCaseName = NameNormalizerHelper.getNormalizeName(featureName,testCaseName);
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(normalizeTestCaseName);
        testCaseReportMap.put(normalizeTestCaseName,testCaseReport);
    }

    public void updateTestCaseReport(String featureName,String testCaseName,Result result){
        TestCaseReport testCaseReport = getTestCaseReport(featureName, testCaseName);
        testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        testCaseReport.setResult(result);
    }

    private TestCaseReport getTestCaseReport(String featureName, String testCaseName) {
        String normalizeTestCaseName = NameNormalizerHelper.getNormalizeName(featureName,testCaseName);
        return testCaseReportMap.get(normalizeTestCaseName);
    }

    public void createTestStepReport(String featureName, String testCaseName, String testStepName){
        ActionReport actionReport = ActionReportHelper.getActionReport(testStepName);
        String normalizeName = NameNormalizerHelper.getNormalizeName(featureName, testCaseName, testStepName);
        actionReportMap.put(normalizeName,actionReport);
    }

    public void updateTestStepReport(ReportDetail reportDetail){
        TestCaseReport testCaseReport = getTestCaseReport(reportDetail.getFeatureName(), reportDetail.getTestCaseName());
        ActionReport actionReport = getActionReport(reportDetail);
        testCaseReport.getActionReports().getActionReports().add(actionReport);
    }


    private ActionReport getActionReport(ReportDetail reportDetail) {
        String normalizeName = NameNormalizerHelper.getNormalizeName(reportDetail.getFeatureName(),reportDetail.getTestCaseName(),reportDetail.getStepName());
        List<Variable> allLogList = getLogList(reportDetail, normalizeName);
        ArrayOfVariable arrayOfVariable = ArrayOfVariableHelper.getArrayOfVariable(allLogList);
        ActionReport actionReport = actionReportMap.get(normalizeName);
        actionReport.setLogMap(arrayOfVariable);
        actionReport.getScreenshots().getScreenshotReports().addAll(getScreeenshots(normalizeName));
        actionReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
        actionReport.setResult(reportDetail.getResult());
        return actionReport;
    }

    private List<Variable> getLogList(ReportDetail reportDetail, String normalizeName) {
        List<Variable> infoLogList = getLog(Logger.INFO, "info", normalizeName);
        List<Variable> warnLogList = getLog(Logger.WARN, "warn", normalizeName);
        List<Variable> errorLogList = getLog(Logger.ERROR, "error", normalizeName);
        List<Variable> fatalLogList = getLog(Logger.FATAL, "fatal", normalizeName);
        if(reportDetail.getThrowable()!=null){
            fatalLogList.add(VariableHelper.getVariable("fatal",ExceptionUtils.getStackTrace(reportDetail.getThrowable())));
        }
        List<Variable> allLogList = Stream.of(infoLogList, warnLogList, errorLogList, fatalLogList).flatMap(Collection::stream).collect(Collectors.toList());
        return allLogList;
    }

    private List<Variable> getLog(Map<String, List<String>> map, String severity, String normalizeName) {
        List<String> informationList = map.get(normalizeName);
        if(informationList == null){
            return new ArrayList<>();
        }
        List<Variable> infoList = informationList.stream()
                .map(information ->
                    VariableHelper.getVariable(severity,information)
                ).collect(Collectors.toList());
        return infoList;
    }

    private List<ScreenshotReport> getScreeenshots(String normalizeName) {
        List<byte[]> screeenshotList = fr.axa.automation.webengine.helper.ScreenshotHelper.SCREENSHOT.get(normalizeName);
        if(screeenshotList == null){
            return new ArrayList<>();
        }
        List<ScreenshotReport> screenshotReportList = screeenshotList.stream()
                .map(screen ->
                    ScreenshotHelper.getScreenshotReport("",screen)
                ).collect(Collectors.toList());
        return screenshotReportList;
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
        reportHelper.generateReports(testSuiteReport,applicationName, FileUtil.getPathWithTargetDirectory(ReportPathConstant.REPORT_DIRECTORY_NAME.getValue()));
    }
}
