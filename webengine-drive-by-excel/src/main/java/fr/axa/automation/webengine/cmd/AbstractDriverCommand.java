package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.constante.LocatingBy;
import fr.axa.automation.webengine.constante.TargetKey;
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
    StringBuffer stringBuffer = new StringBuffer();

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
        String value = "";
        switch (locatingBy) {
            case BY_ID:
                value = commandData.getTargetList().get(TargetKey.ID);
                return StringUtils.isNotEmpty(value) ? value : StringUtils.EMPTY;
//            case BY_NAME:
//            case BY_CLASS_NAME:
//            case BY_LINK_TEXT:
//            case BY_TAG_NAME:
//            case BY_CSS_SELECTOR:
            case BY_XPATH:
                value = commandData.getTargetList().get(TargetKey.XPATH);
                return StringUtils.isNotEmpty(value) ? value : StringUtils.EMPTY;
            default:
                return StringUtils.EMPTY;
        }
    }

    public CommandResult execute(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList) throws WebEngineException {
        ActionReport actionReport = ActionReportHelper.getActionReport(commandData.getName());
        getStringBuffer().append(Constante.CR_LF.getValue()).append("Executed command : ").append(commandData);
        try {
            String dataTestColumName = ((TestCaseDriveByExcelContext) testCaseContext).getDataTestColumnName();
            if (CommandDataHelper.canExecuteDataTestColumn(commandData.getDataTestReferenceList(), dataTestColumName)) {
                executeCmd(globalApplicationContext, testCaseContext, commandData, commandResultList);
                actionReport.getScreenshots().getScreenshotReports().addAll(getScreenshotReportList());
                actionReport.setResult(Result.PASSED);
                getStringBuffer().append(Constante.CR_LF.getValue()).append("Status :").append(Result.PASSED.value());
            } else {
                actionReport.setResult(Result.IGNORED);
                getStringBuffer().append(Constante.CR_LF.getValue()).append("Warning : ").append(Constante.CR_LF.getValue()).append("Command ignored because the colum data-test-ref contains '!" + dataTestColumName + "'");
            }
            actionReport.setLog(getStringBuffer().toString());
        } catch (Throwable e) {
            actionReport.setResult(Result.FAILED);
            actionReport.getScreenshots().getScreenshotReports().add(screenShot(testCaseContext, ""));
            if (commandData.isOptional()) {
                actionReport.setResult(Result.IGNORED);
                getStringBuffer().append(Constante.CR_LF.getValue()).append("Warning : ").append(Constante.CR_LF.getValue()).append(" Command failed but ignored because this command is optional");
            }
            getStringBuffer().append(Constante.CR_LF.getValue()).append("Exception : ").append(Constante.CR_LF.getValue()).append(ExceptionUtils.getStackTrace(e));
            actionReport.setLog(getStringBuffer().toString());
        } finally {
            actionReport.setEndTime(Calendar.getInstance());
        }
        loggerService.info(getStringBuffer().toString());
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

    protected void selectByValueForInputRadio(String value) throws Exception {
        webElementDescription.scrollToElementAndcheckByValue(value);
    }

    protected void selectByValueOrText(String value) throws Exception {
        webElementDescription.selectByValueOrText(value);
    }
}