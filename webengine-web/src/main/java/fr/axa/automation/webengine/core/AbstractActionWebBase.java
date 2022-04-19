package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.ActionContext;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.helper.EnvironmentVariablesHelper;
import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.helper.TestCaseDataHelper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Calendar;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractActionWebBase extends AbstractActionBase {

    public AbstractActionWebBase() {
        super();
    }

    public AbstractActionWebBase(ActionContext actionDetailContext) {
        super(actionDetailContext);
    }

    @Override
    public ActionReport runAction() throws Exception {
        String erroMessage = "";
        ActionReport actionReport = getActionReport();
        try {
            doAction();
            if(getResult()!=null && (getResult() == Result.FAILED || getResult() == Result.CRITICAL_ERROR)) {
                actionReport.setResult(getResult());
                screenShot("Error in this action "+getClass().getSimpleName());
            }
            actionReport.setResult(Result.PASSED);
        }catch (NoSuchElementException e){
            erroMessage = "Web element not present in this action : "+getClass().getSimpleName();
            screenShot(erroMessage);
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(erroMessage);
            loggerService.error(erroMessage,e);
        }catch (Exception e){
            erroMessage = "Error during exection of act : "+getClass().getSimpleName();
            screenShot(erroMessage);
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(erroMessage);
            loggerService.error(erroMessage,e);
        }finally {
            actionReport.setEndTime(Calendar.getInstance());
            actionReport.getScreenshots().getScreenshotReport().addAll(screenShotList);
//            actionReport.setContextValues(actionDetailContext.getTestCaseData().getData());
        }

        return actionReport;
    }

    public void screenShot(String name) throws WebEngineException{
        String screenshot = ((TakesScreenshot)actionDetailContext.getContext()).getScreenshotAs(OutputType.BASE64);
        ScreenshotReport screenshotReport = ScreenshotHelper.getScreenshotReport(getClass().getSimpleName(),screenshot);
        screenShotList.add(screenshotReport);
    }

    protected Optional<String> getEnvironnementValue(String name){
        Optional<String> value = null;
        Optional<Variable> variable = EnvironmentVariablesHelper.getEnvironnementValue(name, getActionDetailContext().getEnvironmentVariables().getVariable());
        if(variable.isPresent()){
            value = Optional.ofNullable(variable.get().getValue());
        }
        return value;
    }

    protected WebDriver getWebDriver(){
        return ((WebDriver)getActionDetailContext().getContext());
    }

    protected Optional<String> getParameter(String name){
        Optional<String> value = null;
        Optional<Variable> variable = TestCaseDataHelper.getValue(name, getActionDetailContext().getTestCaseData().getData().getVariable());
        if(variable.isPresent()){
            value = Optional.ofNullable(variable.get().getValue());
        }
        return value;
    }

}
