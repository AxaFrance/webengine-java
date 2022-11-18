package fr.axa.automation.webengine.core;


import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.helper.ActionReportDetailHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.object.ActionReportDetail;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractActionExecutor implements IActionExecutor {

    ILoggerService loggerService;

    protected AbstractActionExecutor(ILoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public ActionReportDetail run(IAction action) {
        String errorMessage ;
        String actionName = action.getClass().getSimpleName();
        ActionReportDetail actionReportDetail = ActionReportDetail.builder().build();
        try {
            actionReportDetail = execute(action);
        } catch (Throwable e) {
            errorMessage = "Error during execution of action :" + actionName;
            loggerService.error(errorMessage, e);
            actionReportDetail.getActionReport().setResult(Result.CRITICAL_ERROR);
            actionReportDetail.getActionReport().setLog(ExceptionUtils.getStackTrace(e));
        }
        return actionReportDetail;
    }

    private ActionReportDetail execute(IAction action)  {
        String errorMessage;
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
            } catch (Throwable e) {
                loggerService.error("The action " + actionName + " is failed ",e);
                throw e;
            }
            return ActionReportDetailHelper.getActionReportDetail(actionReport, resultCheckPoint);
        });

        try {
            actionReportDetail = future.get();
            loggerService.info("The action " + actionName + " is finished at "+ LocalDateTime.now());
        } catch (Throwable e) {
            errorMessage = "Exception for action :" + actionName;
            actionReportDetail.getActionReport().setResult(Result.CRITICAL_ERROR);
            actionReportDetail.getActionReport().setLog(ExceptionUtils.getStackTrace(e));
            loggerService.error(errorMessage, e);
            future.cancel(true);
        } finally {
            executorService.shutdown();
        }

        return actionReportDetail;
    }

    private boolean isRunCheckpoint(ActionReport actionReport){
        return (actionReport != null && actionReport.getResult() != Result.IGNORED);
    }
}
