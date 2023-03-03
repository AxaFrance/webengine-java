package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
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

     protected ITestCaseContext createTestCaseContext(String testCaseName, Object webDriver) throws WebEngineException {
          ITestCaseContext testCaseContext = getTestCaseContext();
          testCaseContext.setTestCaseName(testCaseName);
          testCaseContext.setWebDriver(webDriver);
          return testCaseContext;
     }

     public abstract ITestCaseContext getTestCaseContext() throws WebEngineException;

     public abstract Object initializeWebDriver(GlobalApplicationContext globalApplicationContext) throws WebEngineException;

     @Override
     public abstract void cleanUp(ITestCaseContext testCaseContext) ;

     protected Result getResultOfTestCase(List<ActionReportDetail> actionReportDetailList) {
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
