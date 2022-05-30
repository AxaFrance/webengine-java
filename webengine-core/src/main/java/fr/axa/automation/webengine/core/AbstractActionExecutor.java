package fr.axa.automation.webengine.core;


import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.GlobalApplicationContext;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.report.ActionReportDetail;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public abstract class AbstractActionExecutor implements IActionExecutor {

    @Autowired
    LoggerService loggerService;

    public AbstractActionExecutor() {
    }

    public ActionReportDetail run(GlobalApplicationContext globalApplicationContext, IAction action) throws WebEngineException {
        String actionName = action.getClass().getSimpleName();
        ActionReportDetail actionReportDetail;
        try {
            actionReportDetail = execute(globalApplicationContext, action);
        } catch (WebEngineException e) {
            throw new WebEngineException("Exception occurs when running the Action : " + actionName, e);
        }
        return actionReportDetail;
    }

    private boolean isRunCheckpoint(ActionReport actionReport){
        return (actionReport != null && actionReport.getResult() != Result.IGNORED);
    }

    private ActionReportDetail execute(GlobalApplicationContext globalApplicationContext, IAction action) throws WebEngineException {
        String errorMessage = "";
        String actionName = action.getClass().getSimpleName();
        LocalDateTime startTime = LocalDateTime.now();
        ActionReportDetail actionReportDetail = ActionReportDetail.builder().build();

        loggerService.info("The action " + actionName + " is started at " + startTime);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<ActionReportDetail> future = executorService.submit(() -> {
            ActionReport actionReport;
            boolean resultCheckPoint = false;
            try {
                actionReport = action.runAction();
                if (isRunCheckpoint(actionReport)) {
                    resultCheckPoint = action.runCheckpoint();
                }
            } catch (Exception e) {
                loggerService.error("The action " + actionName + " is failed ",e);
                throw e;
            }
            return ActionReportDetail.builder().actionReport(actionReport).resultCheckPoint(resultCheckPoint).build();
        });

        try {
            actionReportDetail = future.get();
            loggerService.info("The action " + actionName + " is finished at "+ LocalDateTime.now());
        } catch (InterruptedException ierr) {
            errorMessage = "Interrupted Exception for action :" + actionName;
            actionReportDetail.getActionReport().setResult(Result.CRITICAL_ERROR);
            actionReportDetail.getActionReport().setLog(errorMessage);
            loggerService.error(errorMessage, ierr);
            throw new WebEngineException(errorMessage,ierr);
        } catch (ExecutionException err) {
            errorMessage = "Execution Exception for action :" + actionName;
            actionReportDetail.getActionReport().setResult(Result.CRITICAL_ERROR);
            actionReportDetail.getActionReport().setLog(errorMessage);
            loggerService.error(errorMessage, err);
            throw new WebEngineException(errorMessage,err);
        }
        executorService.shutdown();
        return actionReportDetail;
    }
}
