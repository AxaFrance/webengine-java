package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
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

     protected AbstractTestCaseContext createTestCaseContext(Object webDriver, String testCaseName) throws WebEngineException {
          AbstractTestCaseContext testCaseContext = getTestCaseContext();
          testCaseContext.setTestCaseName(testCaseName);
          testCaseContext.setWebDriver(webDriver);
          return testCaseContext;
     }

     public abstract AbstractTestCaseContext getTestCaseContext() ;

     public abstract Object initializeWebDriver(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException;

     protected Result getResultOfTestCase(List<ActionReport> actionReportList) {
          Result result = Result.PASSED;
          if(getResultOfAllAction(actionReportList) == Result.FAILED ){
               return Result.FAILED;
          }
          return result;
     }

     protected Result getResultOfAllAction(List<ActionReport> actionReportList) {
          Result result = Result.PASSED;
          if (CollectionUtils.isNotEmpty(actionReportList)) {
               List<ActionReport> actionReportDetailFilterList = actionReportList.stream().filter(actionReport ->
                       actionReport != null &&
                       (actionReport.getResult() == Result.FAILED || actionReport.getResult() == Result.CRITICAL_ERROR)
               ).collect(Collectors.toList());
               return CollectionUtils.isNotEmpty(actionReportDetailFilterList) ? Result.FAILED : result;
          }
          return result;
     }

     protected boolean isIgnoredAllOtherAction(ActionReport actionReport){
          boolean ignored = false;
          if ( actionReport != null ) {
               Result result = actionReport.getResult();
               if (result == Result.CRITICAL_ERROR) {
                    ignored = true;
               }
          }
          return ignored;
     }
}
