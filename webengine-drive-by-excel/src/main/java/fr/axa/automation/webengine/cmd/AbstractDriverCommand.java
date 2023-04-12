package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.constante.LocatingBy;
import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.CommandDataHelper;
import fr.axa.automation.webengine.helper.EvaluateValueHelper;
import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractDriverCommand implements ICommand {

    WebElementDescription webElementDescription;
    List<ScreenshotReport> screenshotReportList = new ArrayList<>();
    String savedData;
    ILoggerService loggerService = LoggerServiceProvider.getInstance();

    public abstract void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList) throws Exception;

    protected WebElementDescription populateWebElement(CommandDataDriveByExcel commandData, AbstractTestCaseContext testCaseContext) {
        return WebElementDescription.builder()
                .useDriver((WebDriver) testCaseContext.getWebDriver())
                .id(populateBySelector(commandData, LocatingBy.BY_ID))
                .name(populateBySelector(commandData, LocatingBy.BY_NAME))
                .className(populateBySelector(commandData, LocatingBy.BY_CLASS_NAME))
                .linkText(populateBySelector(commandData, LocatingBy.BY_LINK_TEXT))
                .tagName(populateBySelector(commandData, LocatingBy.BY_TAG_NAME))
                .cssSelector(populateBySelector(commandData, LocatingBy.BY_CSS_SELECTOR))
                .xPath(populateBySelector(commandData, LocatingBy.BY_XPATH))
                .build();
    }

    protected String populateBySelector(CommandDataDriveByExcel commandData, LocatingBy locatingBy) {
        switch (locatingBy) {
            case BY_ID:
            case BY_NAME:
            case BY_CLASS_NAME:
            case BY_LINK_TEXT:
            case BY_TAG_NAME:
            case BY_CSS_SELECTOR:
            case BY_XPATH:
                String value = commandData.getTargetList().get(locatingBy.getValue());
                return StringUtils.isNotEmpty(value) ? value : StringUtils.EMPTY;

            default:
                return StringUtils.EMPTY;
        }
    }

    public CommandResult execute(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList) throws WebEngineException {
        ActionReport actionReport = ActionReportHelper.getActionReport(commandData.getName());
        String message = Constante.CR_LF.getValue() + "Executed command : " + commandData.toString() ;
        try {
            String dataTestColumName = ((TestCaseDriveByExcelContext) testCaseContext).getDataTestColumnName();
            if (CommandDataHelper.canExecuteDataTestColumn(commandData.getDataTestReferenceList(), dataTestColumName)) {
                executeCmd(globalApplicationContext, testCaseContext, commandData, commandResultList);
                actionReport.getScreenshots().getScreenshotReports().addAll(getScreenshotReportList());
                actionReport.setResult(Result.PASSED);
                message = message + Constante.CR_LF.getValue() + "Status :" + Result.PASSED.value();
            } else {
                actionReport.setResult(Result.IGNORED);
                message = message + Constante.CR_LF.getValue() + "Warning : " + Constante.CR_LF.getValue() + "Command ignored because the colum data-test-ref contains '!" + dataTestColumName + "'" ;
            }
            actionReport.setLog(message);
        } catch (Throwable e) {
            actionReport.setResult(Result.FAILED);
            actionReport.getScreenshots().getScreenshotReports().add(screenShot(testCaseContext, ""));
            if (commandData.isOptional()) {
                actionReport.setResult(Result.IGNORED);
                message = message + Constante.CR_LF.getValue() + "Warning : " + Constante.CR_LF.getValue() + " Command failed but ignored because this command is optional" ;
            }
            actionReport.setLog(message + Constante.CR_LF.getValue() + "Exception : " + Constante.CR_LF.getValue() + ExceptionUtils.getStackTrace(e));
        } finally {
            actionReport.setEndTime(Calendar.getInstance());
        }
        loggerService.info(message);
        return CommandResult.builder().commandData(commandData).actionReport(actionReport).savedData(savedData).build();
    }

    protected ScreenshotReport screenShot(AbstractTestCaseContext testCaseContext, String name) {
        byte[] screenshot = ((TakesScreenshot) testCaseContext.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        return ScreenshotHelper.getScreenshotReport(name, screenshot);
    }

    protected String getValue(TestCaseDriveByExcelContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList) {
        String dataTestColumName = testCaseContext.getDataTestColumnName();
        Map<String, String> dataTestMap = commandData.getDataTestMap();
        if (MapUtils.isNotEmpty(dataTestMap) && StringUtils.isNotEmpty(dataTestMap.get(dataTestColumName))) {
            String originalValue = dataTestMap.get(dataTestColumName);
            return EvaluateValueHelper.evaluateValue(originalValue, commandResultList);
        }
        return null;
    }

    protected String getTextByElement(String value) throws Exception {
        if (webElementDescription.isInputText()) {
            return webElementDescription.getText();
        } else {
            return null;
        }
    }

    protected void selectByValueForInputRadio(String value) throws Exception {
        webElementDescription.focus();
        webElementDescription.checkByValue(value);
    }

    protected void selectByValueOrText(String value) throws Exception {
        webElementDescription.scrollIntoView();
        webElementDescription.selectByValueOrText(value);
    }

    protected void selectByValue(String value) throws Exception {
        webElementDescription.selectByValue(value);
    }

    protected void selectByText(String value) throws Exception {
        webElementDescription.selectByText(value);
    }
}