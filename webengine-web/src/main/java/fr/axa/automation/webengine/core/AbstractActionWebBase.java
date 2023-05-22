package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.context.SharedContext;
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
import fr.axa.automation.webengine.report.builder.ActionReportBuilder;
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

    protected AbstractActionWebBase() {
        super();
    }

    protected AbstractActionWebBase(ActionContext actionDetailContext) {
        super(actionDetailContext);
    }

    @Override
    public ActionReport runAction() throws Exception {
        ActionReportBuilder actionReportBuilder = null;
        String className = getClass().getSimpleName();
        String errorMessage = "Error during execution of this action : " + className;
        ActionReport actionReport = ActionReportHelper.getActionReport(className);
        try {
            doAction();
            actionReport.setResult(Result.PASSED);
            if (isResultFailedOrCriticalError()) {
                actionReport.setResult(getResult());
                screenShot(errorMessage);
            }
        } catch (NoSuchElementException e) {
            errorMessage += "Web element not present. ";
            actionReportBuilder = screenShotAndGetActionReportBuilder(errorMessage,  e);
        } catch (Throwable e) {
            actionReportBuilder = screenShotAndGetActionReportBuilder(errorMessage,  e);
        } finally {
            actionReport.setEndTime(Calendar.getInstance());
            actionReport.getContextValues().getVariables().addAll(SharedContext.CONTEXT_VALUE_LIST);
            actionReport.setLog(information.toString());
            if(actionReportBuilder!=null){
                actionReport.setResult(actionReportBuilder.getResult());
                actionReport.setLog(actionReport.getLog()+actionReportBuilder.getLog());
            }
            actionReport.getScreenshots().getScreenshotReports().addAll(screenShotList);
        }
        return actionReport;
    }

    private ActionReportBuilder screenShotAndGetActionReportBuilder(String errorMessage, Throwable e) throws WebEngineException {
        loggerService.error(errorMessage, e);
        screenShot(errorMessage);
        return ActionReportBuilder.builder().result(Result.CRITICAL_ERROR).log(errorMessage).build();
    }

    public void screenShot() throws WebEngineException {
        screenShot("");
    }

    public void screenShot(String name) {
        byte[] screenshot = ((TakesScreenshot) actionDetailContext.getWebDriver()).getScreenshotAs(OutputType.BYTES);
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

    protected Optional<String> getEnvironnementValue(String name) {
        Optional<Variable> variable = EnvironmentVariablesHelper.getEnvironnementValue(name, getActionDetailContext().getEnvironmentVariables().getVariables());
        return getVaribaleByParameter(variable);
    }

    protected Optional<String> getParameter(String name) {
        Optional<Variable> variable = TestCaseDataHelper.getValue(name, getActionDetailContext().getTestCaseData().getData().getVariables());
        return getVaribaleByParameter(variable);
    }

    private Optional<String> getVaribaleByParameter(Optional<Variable> variable) {
        return (variable.isPresent() && StringUtils.isNotEmpty(variable.get().getValue().trim())) ? Optional.of(variable.get().getValue().trim()) : Optional.empty();
    }
    
    protected String getEnvironnementValueWithException(String name) throws WebEngineException {
        Optional<String> environnementValue = getEnvironnementValue(name);
        if (environnementValue.isPresent()) {
            return environnementValue.get();
        } else {
            throw new WebEngineException("La variable d'environnement " + name + " n'est pas présente");
        }
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
        return ((WebDriver) getActionDetailContext().getWebDriver());
    }

    protected void addInformation(String information){
        this.information.append(information);
    }
}
