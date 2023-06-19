package fr.axa.automation.webengine.cmd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.axa.automation.webengine.constante.ConstantNoCode;
import fr.axa.automation.webengine.constante.LocatingBy;
import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.CommandDataHelper;
import fr.axa.automation.webengine.helper.EvaluateValueHelper;
import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.object.CommandDataNoCode;
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

import java.io.FileReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractDriverCommand implements ICommand {

    WebElementDescription webElementDescription;
    List<ScreenshotReport> screenshotReportList = new ArrayList<>();
    String savedData;
    ILoggerService loggerService = LoggerServiceProvider.getInstance();
    StringBuffer logReport = new StringBuffer();

    public abstract void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception;

//    protected WebElementDescription populateWebElement(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) {
//        return WebElementDescription.builder()
//                .useDriver((WebDriver) testCaseContext.getWebDriver())
//                .id(populateBySelector(globalApplicationContext, LocatingBy.BY_ID, commandData, commandResultList))
//                .name(populateBySelector(globalApplicationContext, LocatingBy.BY_NAME, commandData, commandResultList))
//                .className(populateBySelector(globalApplicationContext, LocatingBy.BY_CLASS_NAME, commandData, commandResultList))
//                .linkText(populateBySelector(globalApplicationContext, LocatingBy.BY_LINK_TEXT, commandData, commandResultList))
//                .tagName(populateBySelector(globalApplicationContext, LocatingBy.BY_TAG_NAME, commandData, commandResultList))
//                .cssSelector(populateBySelector(globalApplicationContext, LocatingBy.BY_CSS_SELECTOR, commandData, commandResultList))
//                .xPath(populateBySelector(globalApplicationContext, LocatingBy.BY_XPATH, commandData, commandResultList))
//                .build();
//    }
//
//    protected String populateBySelector(AbstractGlobalApplicationContext globalApplicationContext, LocatingBy locatingBy, CommandDataNoCode commandData, List<CommandResult> commandResultList) {
//        String value = "";
//        switch (locatingBy) {
//            case BY_ID:
//                value = commandData.getTargetList().get(TargetKey.ID);
//                break;
//            case BY_XPATH:
//                value = commandData.getTargetList().get(TargetKey.XPATH);
//                break;
//            default:
//                return StringUtils.EMPTY;
//        }
//        return StringUtils.isNotEmpty(value) ? EvaluateValueHelper.evaluateValue(globalApplicationContext.getSettings(), value, commandResultList) : StringUtils.EMPTY; //For xpath, id ...etc dynamic
//    }

    protected WebElementDescription populateWebElement(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws  WebEngineException{
        Map.Entry<TargetKey,String> entry = getTargetValue(globalApplicationContext, commandData, commandResultList);
        if(entry==null){
            return WebElementDescription.builder()
                    .useDriver((WebDriver) testCaseContext.getWebDriver())
                    .build();
        }

        switch (entry.getKey()){
            case ID:
                return WebElementDescription.builder()
                        .useDriver((WebDriver) testCaseContext.getWebDriver())
                        .id(entry.getValue())
                        .build();
            case XPATH:
                return WebElementDescription.builder()
                        .useDriver((WebDriver) testCaseContext.getWebDriver())
                        .xPath(entry.getValue())
                        .build();
            case COMBINAISON_OF_LOCATOR:
                ObjectMapper mapper = new ObjectMapper();
                try {
                    WebElementDescription webElementDescription = mapper.readValue(entry.getValue(), WebElementDescription.class);
                    webElementDescription.setUseDriver((WebDriver) testCaseContext.getWebDriver());
                    return webElementDescription;
                } catch (JsonProcessingException e) {
                    throw new WebEngineException("Veuillez vérifier le format de la combinaison de locator : "+entry.getValue(), e);
                }

            default:
                return WebElementDescription.builder()
                        .useDriver((WebDriver) testCaseContext.getWebDriver())
                        .build();
        }
    }

    protected Map.Entry<TargetKey,String> getTargetValue(AbstractGlobalApplicationContext globalApplicationContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) {
        String value;
        Set<TargetKey> targetKeyList = commandData.getTargetList().keySet();
        if (MapUtils.isNotEmpty(commandData.getTargetList()) && targetKeyList.size() == 1) {
            TargetKey targetKey = targetKeyList.iterator().next();
            switch (targetKey) {
                case ID:
                    value = commandData.getTargetList().get(TargetKey.ID);
                    break;
                case XPATH:
                    value = commandData.getTargetList().get(TargetKey.XPATH);
                    break;
                case COMBINAISON_OF_LOCATOR:
                    value = commandData.getTargetList().get(TargetKey.COMBINAISON_OF_LOCATOR);
                    break;
                default:
                    value = StringUtils.EMPTY;
            }
            String evaluateValue = StringUtils.isNotEmpty(value) ? EvaluateValueHelper.evaluateValue(globalApplicationContext.getSettings(), value, commandResultList) : StringUtils.EMPTY; //For xpath, id ...etc dynamic
            return MapUtils.isNotEmpty(commandData.getTargetList()) ? new AbstractMap.SimpleEntry(targetKeyList.iterator().next(), evaluateValue) : null;
        }
        return null;
    }

    public CommandResult execute(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        ActionReport actionReport = ActionReportHelper.getActionReport(commandData.getName());
        getLogReport().append(ConstantNoCode.CR_LF.getValue()).append("Executed command : ").append(commandData);
        try {
            String dataTestColumName = ((TestCaseNoCodeContext) testCaseContext).getDataTestColumnName();
            if (CommandDataHelper.canExecuteDataTestColumn(commandData.getDataTestReferenceList(), dataTestColumName)) {
                executeCmd(globalApplicationContext, testCaseContext, commandData, commandResultList);
                actionReport.getScreenshots().getScreenshotReports().addAll(getScreenshotReportList());
                actionReport.setResult(Result.PASSED);
                getLogReport().append(ConstantNoCode.CR_LF.getValue()).append("Status :").append(Result.PASSED.value());
            } else {
                actionReport.setResult(Result.IGNORED);
                getLogReport().append(ConstantNoCode.CR_LF.getValue()).append("Warning : ").append(ConstantNoCode.CR_LF.getValue()).append("Command ignored because the colum data-test-ref contains '!" + dataTestColumName + "'");
            }
            actionReport.setLog(getLogReport().toString());
        } catch (Throwable e) {
            actionReport.setResult(Result.FAILED);
            actionReport.getScreenshots().getScreenshotReports().add(screenShot(testCaseContext, ""));
            if (commandData.isOptional()) {
                actionReport.setResult(Result.IGNORED);
                getLogReport().append(ConstantNoCode.CR_LF.getValue()).append("Warning : ").append(ConstantNoCode.CR_LF.getValue()).append(" Command failed but ignored because this command is optional");
            }
            getLogReport().append(ConstantNoCode.CR_LF.getValue()).append("Exception : ").append(ConstantNoCode.CR_LF.getValue()).append(ExceptionUtils.getStackTrace(e));
            actionReport.setLog(getLogReport().toString());
        } finally {
            actionReport.setEndTime(Calendar.getInstance());
        }
        loggerService.info(getLogReport().toString());
        return CommandResult.builder().commandData(commandData).actionReport(actionReport).savedData(savedData).build();
    }

    protected ScreenshotReport screenShot(AbstractTestCaseContext testCaseContext, String name) {
        byte[] screenshot = ((TakesScreenshot) testCaseContext.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        return ScreenshotHelper.getScreenshotReport(name, screenshot);
    }

    protected String getValue(AbstractGlobalApplicationContext globalApplicationContext, TestCaseNoCodeContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) {
        String dataTestColumName = testCaseContext.getDataTestColumnName();
        Map<String, String> dataTestMap = commandData.getDataTestMap();
        if (MapUtils.isNotEmpty(dataTestMap) && StringUtils.isNotEmpty(dataTestMap.get(dataTestColumName))) {
            String originalValue = dataTestMap.get(dataTestColumName);
            return EvaluateValueHelper.evaluateValue(globalApplicationContext.getSettings(), originalValue, commandResultList);
        }
        return null;
    }

}