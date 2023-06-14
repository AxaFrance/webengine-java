package fr.axa.automation.webengine.core;


import fr.axa.automation.webengine.api.ITestCaseWebExecutor;
import fr.axa.automation.webengine.api.ITestStepWebExecutor;
import fr.axa.automation.webengine.context.SharedContext;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.global.TestCaseWebContext;
import fr.axa.automation.webengine.helper.ActionReportDetailHelper;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.TestDataHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import fr.axa.automation.webengine.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Qualifier("testCaseWebExecutor")
public class TestCaseWebExecutor extends AbstractTestCaseWebExecutor implements ITestCaseWebExecutor {

    @Autowired
    public TestCaseWebExecutor(@Qualifier("testStepWebExecutor")ITestStepExecutor testStepExecutor, GlobalConfiguration globalConfiguration, ILoggerService loggerService ) {
        super(testStepExecutor, globalConfiguration, loggerService);
    }

    @Override
    public AbstractTestCaseContext getTestCaseContext() {
        return TestCaseWebContext.builder().build();
    }

    public AbstractTestCaseContext initialize(AbstractGlobalApplicationContext globalApplicationContext, String testCaseName, ITestCase testCase) throws WebEngineException {
        Object webDriver = initializeWebDriver(globalApplicationContext);
        return createTestCaseContext(webDriver, testCaseName, testCase );
    }

    protected AbstractTestCaseContext createTestCaseContext(Object webDriver, String testCaseName, ITestCase testCase ) throws WebEngineException {
        AbstractTestCaseContext testCaseContext = super.createTestCaseContext(webDriver,testCaseName);
        ((TestCaseWebContext)testCaseContext).setTestCaseToExecute(testCase);
        return testCaseContext;
    }

    public TestCaseReport run(AbstractGlobalApplicationContext globalAppContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        GlobalApplicationContext globalApplicationContext = (GlobalApplicationContext)globalAppContext;
        String testCaseName = testCaseContext.getTestCaseName();
        TestCaseReport testCaseReport = TestCaseReportHelper.createTestCaseReport(testCaseName);

        List<ActionReportDetail> actionReportDetailList = new ArrayList<>();

        List<TestData> testDataList = globalApplicationContext.getTestDataList();
        Optional<TestData> testDataByTestCase = TestDataHelper.getDataOfTestCase(testDataList,testCaseName);
        if(!testDataByTestCase.isPresent()){
            loggerService.info("Be careful, no test data for this test case : "+testCaseName);
        }

        try {
            actionReportDetailList.addAll(runTestStep(globalApplicationContext, testCaseContext));
        }catch (Throwable e){
            testCaseReport.setResult(Result.FAILED);
            loggerService.error("Error during execution of test case : "+testCaseName,e);
        }finally {
            testCaseReport.getActionReports().getActionReports().addAll(ActionReportHelper.getActionReportList(actionReportDetailList));
            testCaseReport.setTestData(testDataByTestCase.map(TestData::getData).orElse(null));
            testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
            testCaseReport.setResult(getResultOfTestCaseWeb(actionReportDetailList));          }
        return testCaseReport;
    }

    protected List<ActionReportDetail> runTestStep(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext) throws WebEngineException {
        String testCaseName = testCaseContext.getTestCaseName();
        ActionReportDetail actionReportDetail;
        ActionReport actionReport = new ActionReport();
        List<ActionReportDetail> actionReportDetailList = new ArrayList<>();
        List<? extends ITestStep> testStepList = ((TestCaseWebContext)testCaseContext).getTestCaseToExecute().getTestStepList();
        boolean ignoredAllNextTestStep = false;
        String testStepName = "" ;

        if(CollectionUtils.isEmpty(testStepList)){
            throw new WebEngineException("No Test step found for this test case :"+testCaseName);
        }

        SharedContext.CONTEXT_VALUE_LIST.clear();
        try {
            for (ITestStep testStep : testStepList){
                testStepName = testStep.getClass().getSimpleName();
                actionReport = new ActionReport();
                actionReport.setName(testStepName);
                actionReport.setId(UUID.randomUUID().toString());

                if(((TestCaseWebContext)testCaseContext).getTestCaseToExecute().isIgnoredAllTestStep() || ignoredAllNextTestStep){
                    actionReport.setResult(Result.IGNORED);
                    actionReportDetailList.add(ActionReportDetailHelper.getActionReportDetail(actionReport, true));
                    loggerService.info("All test step are ignored. Test case is : "+ testCaseName +" and test step name is : "+ testStep.getClass().getName());
                    continue;
                }

                actionReportDetail = ((ITestStepWebExecutor)testStepExecutor).run(globalApplicationContext,testCaseContext,testStep);
                ignoredAllNextTestStep = verifyCheckpoint(actionReportDetail);
                actionReportDetailList.add(actionReportDetail);
            }
        }catch (Throwable e){
            loggerService.info("Fatal exception during step : "+ testStepName +" and test case name is : "+ testCaseName +". All test step are cancelled.");
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(ExceptionUtils.getStackTrace(e));
            actionReportDetailList.add(ActionReportDetailHelper.getActionReportDetail(actionReport, false));
        }
        return actionReportDetailList;
    }

    protected Result getResultOfTestCaseWeb(List<ActionReportDetail> actionReportDetailList) {
        Result result = Result.PASSED;
        if(getResultActionReport(actionReportDetailList) == Result.FAILED || getResultCheckPoint(actionReportDetailList) == Result.FAILED ){
            return Result.FAILED;
        }
        return result;
    }

    protected Result getResultActionReport(List<ActionReportDetail> actionReportDetailList) {
        Result result = Result.PASSED;
        if (CollectionUtils.isNotEmpty(actionReportDetailList)) {
            List<ActionReportDetail> actionReportDetailFilterList = actionReportDetailList.stream().filter(actionReportDetail ->
                    actionReportDetail != null &&
                    actionReportDetail.getActionReport() != null &&
                    (actionReportDetail.getActionReport().getResult() == Result.FAILED || actionReportDetail.getActionReport().getResult() == Result.CRITICAL_ERROR)
            ).collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(actionReportDetailFilterList) ? Result.FAILED : result;
        }
        return result;
    }

    protected Result getResultCheckPoint(List<ActionReportDetail> actionReportDetailList) {
        Result result = Result.PASSED;
        if (CollectionUtils.isNotEmpty(actionReportDetailList)) {
            List<ActionReportDetail> actionReportDetailFilterList = actionReportDetailList.stream().filter(actionReportDetail ->
                    actionReportDetail != null &&
                    !actionReportDetail.isResultCheckPoint()
            ).collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(actionReportDetailFilterList) ? Result.FAILED : result;
        }
        return result;
    }

    private boolean verifyCheckpoint(ActionReportDetail actionReportDetail){
        boolean ignored = false;
        if (actionReportDetail != null && actionReportDetail.getActionReport() != null) {
            Result result = actionReportDetail.getActionReport().getResult();
            if (result == Result.CRITICAL_ERROR || !actionReportDetail.isResultCheckPoint()) {
                ignored = true;
            }
        }
        return ignored;
    }
}
