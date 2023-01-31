package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.context.SharedContext;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.ITestCaseContext;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.TestCaseReport;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.helper.ActionReportDetailHelper;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.TestDataHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.TestCaseReportHelper;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import fr.axa.automation.webengine.util.DateUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestCaseExecutor implements ITestCaseExecutor {

     ITestStepExecutor testStepExecutor;
     GlobalConfigProperties globalConfigProperties;
     ILoggerService loggerService;

     protected AbstractTestCaseExecutor(ITestStepExecutor testStepExecutor, GlobalConfigProperties globalConfigProperties, ILoggerService loggerService) {
          this.testStepExecutor = testStepExecutor;
          this.globalConfigProperties = globalConfigProperties;
          this.loggerService = loggerService;
     }

     public ITestCaseContext initialize(GlobalApplicationContext globalApplicationContext, String testCaseName, ITestCase testCase) throws WebEngineException {
          ITestCaseContext testCaseContext = initializeTestCaseContext(globalApplicationContext);
          testCaseContext.setTestCaseName(testCaseName);
          testCaseContext.setTestCaseToExecute(testCase);
          return testCaseContext;
     }

     public abstract ITestCaseContext initializeTestCaseContext(GlobalApplicationContext globalApplicationContext) throws WebEngineException;

     @Override
     public abstract void cleanUp(ITestCaseContext testCaseContext) ;

     @Override
     public TestCaseReport run(GlobalApplicationContext globalApplicationContext,ITestCaseContext testCaseContext) throws WebEngineException {
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
               testCaseReport.setResult(getResultOfTestCase(actionReportDetailList));          }
          return testCaseReport;
     }

     protected List<ActionReportDetail> runTestStep(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext) throws WebEngineException {
          String testCaseName = testCaseContext.getTestCaseName();
          ActionReportDetail actionReportDetail;
          ActionReport actionReport = new ActionReport();
          List<ActionReportDetail> actionReportDetailList = new ArrayList<>();
          List<? extends ITestStep> testStepList = testCaseContext.getTestCaseToExecute().getTestStepList();
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

                    if(testCaseContext.getTestCaseToExecute().isIgnoredAllTestStep() || ignoredAllNextTestStep){
                         actionReport.setResult(Result.IGNORED);
                         actionReportDetailList.add(ActionReportDetailHelper.getActionReportDetail(actionReport, true));
                         loggerService.info("All test step are ignored. Test case is : "+ testCaseName +" and test step name is : "+ testStep.getClass().getName());
                    }else{
                         actionReportDetail = testStepExecutor.run(globalApplicationContext,testCaseContext,testStep);
                         ignoredAllNextTestStep = verifyCheckpoint(actionReportDetail);
                         actionReportDetailList.add(actionReportDetail);
                    }
               }
          }catch (Throwable e){
               loggerService.info("Fatal exception during step : "+ testStepName +" and test case name is : "+ testCaseName +". All test step are cancelled.");
               actionReport.setResult(Result.CRITICAL_ERROR);
               actionReport.setLog(ExceptionUtils.getStackTrace(e));
               actionReportDetailList.add(ActionReportDetailHelper.getActionReportDetail(actionReport, false));
          }
          return actionReportDetailList;
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

     private Result getResultOfTestCase(List<ActionReportDetail> actionReportDetailList) {
          Result result = Result.PASSED;
          if(getResultActionReport(actionReportDetailList) == Result.FAILED || getResultCheckPoint(actionReportDetailList) == Result.FAILED ){
               return Result.FAILED;
          }
          return result;
     }

     private Result getResultActionReport(List<ActionReportDetail> actionReportDetailList) {
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

     private Result getResultCheckPoint(List<ActionReportDetail> actionReportDetailList) {
          Result result = Result.PASSED;
          if(CollectionUtils.isNotEmpty(actionReportDetailList)){
               List<ActionReportDetail> actionReportDetailFilterList = actionReportDetailList.stream().filter(actionReportDetail ->
                       actionReportDetail!=null &&
                       !actionReportDetail.isResultCheckPoint()
               ).collect(Collectors.toList());
               return CollectionUtils.isNotEmpty(actionReportDetailFilterList) ? Result.FAILED : result;
          }
          return result;
     }
}
