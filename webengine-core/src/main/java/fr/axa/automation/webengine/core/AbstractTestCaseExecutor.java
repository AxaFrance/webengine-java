package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.general.IVariableConstante;
import fr.axa.automation.webengine.generated.*;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.SharedContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestCaseExecutor implements ITestCaseExecutor {

     LoggerService loggerService;
     ITestStepExecutor testStepExecutor;

     public AbstractTestCaseExecutor(LoggerService loggerService, ITestStepExecutor testStepExecutor) {
          this.loggerService = loggerService;
          this.testStepExecutor = testStepExecutor;
     }

     @Override
     public abstract Object initialize(GlobalApplicationContext globalApplicationContext) throws WebEngineException;

     @Override
     public abstract void cleanUp(Object object) ;

     @Override
     public TestCaseReport run(GlobalApplicationContext globalApplicationContext, String testCaseName, ITestCase testCase) throws WebEngineException {
          LocalDateTime startTime = LocalDateTime.now();
          TestCaseReport testCaseReport = new TestCaseReport();
          List<ActionReportDetail> actionReportDetailList = new ArrayList<>();

          List<TestData> testDataList = globalApplicationContext.getTestDataList();
          Optional<TestData> testDataByTestCase = TestDataUtil.getDataOfTestCase(testDataList,testCaseName);
          if(!testDataByTestCase.isPresent()){
               loggerService.info("No test data for test case : "+testCaseName);
          }

          Variable variable = TestDataUtil.getVariableOfTestCase(testDataList,testCaseName, IVariableConstante.UNIQUE_ID.getValue());
          if(variable!=null){
               String uniqueId = variable.getValue();
               loggerService.info("Test case name with uniqueId : "+uniqueId);
          }

          try {
               Object object = initialize(globalApplicationContext);
               actionReportDetailList.addAll(runAllTestStep(globalApplicationContext, object, testCaseName, testCase));
               cleanUp(object);
          }catch (WebEngineException e){
               testCaseReport.setResult(Result.FAILED);
               loggerService.error("Error during execution of test case : "+testCaseName,e);
          }finally {
               testCaseReport.setTestName(testCaseName);
               testCaseReport.setStartTime(DateUtil.localDateTimeToCalendar(startTime));
               testCaseReport.setEndTime(DateUtil.localDateTimeToCalendar(LocalDateTime.now()));
               testCaseReport.setTestData(testDataByTestCase.map(TestData::getData).orElse(null));
               testCaseReport.setActionReports(new ArrayOfActionReport());
               testCaseReport.getActionReports().getActionReport().addAll(ActionReportHelper.getArrayOfActionReport(actionReportDetailList));
               testCaseReport.setResult(getResultOfTestCase(actionReportDetailList));
          }

          return testCaseReport;
     }

     protected List<ActionReportDetail> runAllTestStep(GlobalApplicationContext globalApplicationContext, Object object, String testCaseName, ITestCase testCase) throws WebEngineException {
          ActionReportDetail actionReportDetail;
          ActionReport actionReport = new ActionReport();
          List<ActionReportDetail> actionReportDetailList = new ArrayList<>();
          List<? extends ITestStep> testStepList = testCase.getTestStepList();
          boolean ignoredAllTestStep = false;
          String testStepName = "";

          if(CollectionUtils.isEmpty(testStepList)){
               throw new WebEngineException("No Test step found for this test case :"+testCaseName);
          }

          try {
               SharedContext.CONTEXT_VALUE_LIST.clear();
               for (ITestStep testStep :testStepList){
                    testStepName = testStep.getClass().getSimpleName();
                    actionReport = new ActionReport();
                    actionReport.setName(testStepName);
                    if(testCase.isIgnoredAllTestStep() || ignoredAllTestStep){
                         actionReport.setResult(Result.IGNORED);
                         actionReportDetailList.add(ActionReportDetail.builder().actionReport(actionReport).resultCheckPoint(true).build());
                         loggerService.info("All test step are ignored. Test case is : " + testCaseName+" and test step name is : "+testStep.getClass().getName());
                    }else{
                         actionReportDetail = runTestStep(globalApplicationContext,object,testCaseName,testStep);
                         ignoredAllTestStep = verifyCheckpoint(actionReportDetail);
                         actionReportDetailList.add(actionReportDetail);
                    }
               }
          }catch (WebEngineException e){
               loggerService.info("Fatal exception during step : " + testStepName+" and test case name is : "+testCaseName+". All test step are cancelled.");
               actionReport.setResult(Result.CRITICAL_ERROR);
               actionReport.setLog(e.getMessage());
               actionReportDetailList.add(ActionReportDetail.builder().actionReport(actionReport).resultCheckPoint(true).build());
          }

          return actionReportDetailList;
     }

     private Result getResultOfTestCase(List<ActionReportDetail> actionReportDetailList) {
          Result result = null;
          if(CollectionUtils.isNotEmpty(actionReportDetailList)){
               for (ActionReportDetail actionReportDetail:actionReportDetailList) {
                    if (actionReportDetail != null && actionReportDetail.getActionReport() != null) {
                         result = actionReportDetail.getActionReport().getResult();
                         if (actionReportDetail.isResultCheckPoint() && (result == Result.FAILED || result == Result.CRITICAL_ERROR)) {
                              result = Result.FAILED;
                              break;
                         }
                         if(!actionReportDetail.isResultCheckPoint()) {
                              result = Result.FAILED;
                              break;
                         }
                    }
               }
          }
          return result;
     }

     private boolean verifyCheckpoint(ActionReportDetail actionReportDetail){
          boolean ignored = false;
          if (actionReportDetail != null && actionReportDetail.getActionReport() != null) {
               Result result = actionReportDetail.getActionReport().getResult();
               if (actionReportDetail.isResultCheckPoint() && result == Result.CRITICAL_ERROR) {
                    ignored = true;
               }
               if(!actionReportDetail.isResultCheckPoint()) {
                    ignored = true;
               }
          }
          return ignored;
     }

     protected ActionReportDetail runTestStep(GlobalApplicationContext globalApplicationContext, Object object, String testCaseName, ITestStep testStep) throws WebEngineException {
          return testStepExecutor.run(globalApplicationContext,object,testCaseName,testStep);
     }
}
