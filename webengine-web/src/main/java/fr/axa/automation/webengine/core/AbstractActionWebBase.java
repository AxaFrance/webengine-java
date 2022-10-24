package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.ActionContext;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.EnvironmentVariablesHelper;
import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.helper.TestCaseDataHelper;
import fr.axa.automation.webengine.util.SharedContext;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
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
        ActionReport actionReport = ActionReportHelper.getActionReport(getClass().getSimpleName());
        try {
            doAction();
            actionReport.setResult(Result.PASSED);
            if (getResult() != null && (getResult() == Result.FAILED || getResult() == Result.CRITICAL_ERROR)) {
                actionReport.setResult(getResult());
                screenShot("Error in this action " + getClass().getSimpleName());
            }
        } catch (NoSuchElementException e) {
            erroMessage = "Web element not present in this action : " + getClass().getSimpleName();
            screenShot(erroMessage);
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(erroMessage);
            loggerService.error(erroMessage, e);
        } catch (Throwable e) {
            erroMessage = "Error during execution of act : " + getClass().getSimpleName();
            screenShot(erroMessage);
            actionReport.setResult(Result.CRITICAL_ERROR);
            actionReport.setLog(erroMessage);
            loggerService.error(erroMessage, e);
        } finally {
            actionReport.setEndTime(Calendar.getInstance());
            actionReport.getContextValues().getVariable().addAll(SharedContext.CONTEXT_VALUE_LIST);
            actionReport.setLog(information.toString());
            actionReport.getScreenshots().getScreenshotReport().addAll(screenShotList);
        }

        return actionReport;
    }

    public void screenShot(String name) throws WebEngineException {
        byte[] screenshot = ((TakesScreenshot) actionDetailContext.getContext()).getScreenshotAs(OutputType.BYTES);
        ScreenshotReport screenshotReport = ScreenshotHelper.getScreenshotReport(name, screenshot);
        screenShotList.add(screenshotReport);
    }

    public void screenShot(AbstractElementDescription elementDescription) throws WebEngineException {
        try {
            byte[] screenshot = elementDescription.getScreenshot();
            ScreenshotReport screenshotReport = ScreenshotHelper.getScreenshotReport("Error message", screenshot);
            screenShotList.add(screenshotReport);
        } catch (Exception e) {
           throw new WebEngineException("Erreur lors du screenshot",e);
        }
    }


    public void screenShot() throws WebEngineException {
        screenShot("");
    }

    protected Optional<String> getEnvironnementValue(String name) {
        Optional<String> value = Optional.empty();
        Optional<Variable> variable = EnvironmentVariablesHelper.getEnvironnementValue(name, getActionDetailContext().getEnvironmentVariables().getVariable());
        if (!variable.isPresent() || StringUtils.isEmpty(variable.get().getValue().trim())) {
            value = Optional.empty();
        }else{
            value = Optional.of(variable.get().getValue());
        }
        return value;
    }

    protected String getEnvironnementValueWithException(String name) throws WebEngineException {
        Optional<String> environnementValue = getEnvironnementValue(name);
        if (environnementValue.isPresent()) {
            return environnementValue.get();
        } else {
            throw new WebEngineException("La variable d'environnement " + name + " n'est pas présente");
        }
    }

    protected Optional<String> getParameter(String name) {
        Optional<String> value = Optional.empty();
        Optional<Variable> variable = TestCaseDataHelper.getValue(name, getActionDetailContext().getTestCaseData().getData().getVariable());
        if (!variable.isPresent() || StringUtils.isEmpty(variable.get().getValue().trim())) {
            value = Optional.empty();
        }else{
            value = Optional.of(variable.get().getValue());
        }
        return value;
    }

    protected String getParameterWithException(String name) throws WebEngineException {
        Optional<String> environnement = getParameter(name);
        if (environnement.isPresent()) {
            return environnement.get();
        } else {
            throw new WebEngineException("La variable " + name + " n'est pas présente");
        }
    }

    protected WebDriver getWebDriver() {
        return ((WebDriver) getActionDetailContext().getContext());
    }

    protected void addInformation(String information){
        this.information.append(information);
    }
}
