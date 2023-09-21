package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractTestCaseExecutor implements ITestCaseExecutor {

     ITestStepExecutor testStepExecutor;
     GlobalConfiguration globalConfiguration;
     ILoggerService loggerService;

     protected AbstractTestCaseExecutor(ITestStepExecutor testStepExecutor, GlobalConfiguration globalConfiguration, ILoggerService loggerService) {
          this.testStepExecutor = testStepExecutor;
          this.globalConfiguration = globalConfiguration;
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
          List<Result> subReportResultList = new ArrayList<>();
          List<ActionReport> actionReportFailedList = new ArrayList<>();
          Result result = Result.PASSED;
          if (CollectionUtils.isNotEmpty(actionReportList)) {
               for (ActionReport actionReport : actionReportList) {
                    if(actionReport != null && (actionReport.getResult() == Result.FAILED || actionReport.getResult() == Result.CRITICAL_ERROR)){
                         actionReportFailedList.add(actionReport);
                    }
                    if(actionReport != null && actionReport.getSubActionReports()!=null && CollectionUtils.isNotEmpty(actionReport.getSubActionReports().getActionReports())){
                         subReportResultList.add(getResultOfAllAction(actionReport.getSubActionReports().getActionReports()));
                    }
               }
               return CollectionUtils.isNotEmpty(actionReportFailedList) || subReportResultList.contains(Result.FAILED) ? Result.FAILED : result;
          }
          return result;
     }

     protected boolean isIgnoredAllOtherAction(ActionReport actionReport) {
          boolean ignored = false;
          if (actionReport != null && (actionReport.getResult() == Result.CRITICAL_ERROR || actionReport.getResult() == Result.FAILED)) {
               ignored = true;
          }
          return ignored;
     }
}
