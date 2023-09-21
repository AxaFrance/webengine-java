package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.context.SharedContext;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.global.ActionContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractActionBase implements IAction {

    ILoggerService loggerService ;
    ActionContext actionDetailContext;
    List<ScreenshotReport> screenShotList;
    Result result;
    StringBuilder information;

    protected AbstractActionBase() {
        loggerService = new LoggerService();
        screenShotList = new ArrayList<>();
        information = new StringBuilder();
    }

    protected AbstractActionBase(ActionContext actionDetailContext) {
        this();
        this.actionDetailContext = actionDetailContext;
    }

    @Override
    public ActionReport runAction() throws Exception {
        ActionReport actionReport = ActionReportHelper.getActionReport(getClass().getSimpleName());
        String errorMessage = "Error during execution of +"+getClass().getSimpleName()+" class, method doAction";
        try{
            doAction();
            if(isResultFailedOrCriticalError()){
                actionReport.setResult(getResult());
                screenShot(errorMessage);
            }
        }catch (Throwable throwable){
            screenShot(errorMessage);
        }finally {
            actionReport.setEndTime(Calendar.getInstance());
        }
        return actionReport;
    }

    protected boolean isResultFailedOrCriticalError() {
        return getResult() != null && (getResult() == Result.FAILED || getResult() == Result.CRITICAL_ERROR);
    }

    @Override
    public boolean runCheckpoint() throws Exception{
        boolean checkpoint;
        String errorMessage = "Error during execution of +"+getClass().getSimpleName()+" class, method doCheckpoint";
        try {
            checkpoint = doCheckpoint();
            if(!checkpoint){
                screenShot(errorMessage);
            }
        }catch (Throwable exception){
            checkpoint = false;
            loggerService.error(errorMessage,exception);
            screenShot("Error in this action +"+getClass().getSimpleName()+", phase doCheckpoint");
        }
        return checkpoint;
    }

    protected void setContextValue(Variable contextValue){
        SharedContext.CONTEXT_VALUE_LIST.add(contextValue);
    }

    protected String getContexteValue(String contextName){
        List<Variable> contextFoundList = SharedContext.CONTEXT_VALUE_LIST.stream().filter(variable -> variable.getName().equalsIgnoreCase(contextName)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(contextFoundList)?contextFoundList.get(0).getValue():null;
    }

}
