package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.ActionContext;
import fr.axa.automation.webengine.generated.*;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.util.SharedContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractActionBase implements IAction {
    LoggerService loggerService = new LoggerService();
    ActionContext actionDetailContext;
    List<ScreenshotReport> screenShotList = new ArrayList<>();
    Result result;
    StringBuffer information = new StringBuffer();


    public AbstractActionBase() {
    }

    public AbstractActionBase(ActionContext actionDetailContext) {
        this.actionDetailContext = actionDetailContext;
    }

    @Override
    public ActionReport runAction() throws Exception {
        ActionReport actionReport = ActionReportHelper.getActionReport(getClass().getSimpleName());
        doAction();
        if(getResult()!=null && (getResult() == Result.FAILED || getResult() == Result.CRITICAL_ERROR)){
            screenShot("Error in this step "+getClass().getSimpleName());
        }
        actionReport.setEndTime(Calendar.getInstance());
        return actionReport;
    }

    @Override
    public abstract void doAction() throws Exception;

    @Override
    public boolean runCheckpoint() throws Exception{
        boolean checkpoint = true ;
        try {
            checkpoint = doCheckpoint();
            if(!checkpoint){
                screenShot("Error in this action +"+getClass().getSimpleName()+", phase doCheckpoint");
            }
        }catch (Exception exception){
            checkpoint = false;
            screenShot("Error in this action +"+getClass().getSimpleName()+", phase doCheckpoint");
        }
        return checkpoint;
    }

    public abstract boolean doCheckpoint() throws Exception;

    @Override
    public abstract void screenShot(String name) throws WebEngineException;

    protected void setContextValue(Variable contextValue){
        SharedContext.CONTEXT_VALUE_LIST.add(contextValue);
    }

    protected String getContexteValue(String contextName){
        for (Variable variable: SharedContext.CONTEXT_VALUE_LIST) {
            if(variable.getName().equalsIgnoreCase(contextName)){
                return variable.getValue();
            }
        }
        return null;
    }
}
