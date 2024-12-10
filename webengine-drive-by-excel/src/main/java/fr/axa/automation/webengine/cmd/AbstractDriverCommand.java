package fr.axa.automation.webengine.cmd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.ArrayOfVariable;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.DriverContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.CommandDataHelper;
import fr.axa.automation.webengine.helper.CommandResultHelper;
import fr.axa.automation.webengine.helper.EvaluateValueHelper;
import fr.axa.automation.webengine.helper.GlobalConfigPropertiesHelper;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.helper.ScreenshotHelper;
import fr.axa.automation.webengine.util.BrowserFactory;
import fr.axa.automation.webengine.util.ListUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractDriverCommand implements ICommand {
    static {
        System.setProperty("java.awt.headless", "false");
    }

    WebElementDescription webElementDescription;
    List<ScreenshotReport> screenshotReportList = new ArrayList<>();
    DriverContext driverContext;
    String savedData;


    ILoggerService loggerService = LoggerServiceProvider.getInstance();
    ArrayOfVariable logReport = new ArrayOfVariable();

    public abstract void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception;

    public WebDriver initializeWebDriver(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
       return initializeWebDriver(globalApplicationContext, true);
    }

    public WebDriver initializeWebDriver(AbstractGlobalApplicationContext globalApplicationContext, boolean deleteCookie) throws WebEngineException {
        return getDriver(globalApplicationContext,false, deleteCookie);
    }

    public WebDriver initializeIncognitoWebDriver(AbstractGlobalApplicationContext globalApplicationContext) throws WebEngineException {
        return getDriver(globalApplicationContext,true, true);
    }

    public WebDriver getDriver(AbstractGlobalApplicationContext globalApplicationContext,boolean incognito, boolean deleteCookie) throws WebEngineException {
        try {
            GlobalConfiguration globalConfiguration = GlobalConfigPropertiesHelper.getGlobalConfigProperties(globalApplicationContext.getSettings());
            Optional<WebDriver> optional ;
            if(incognito){
                 optional = BrowserFactory.getIncognitoDriver(globalConfiguration);
            }else {
                optional = BrowserFactory.getDriver(globalConfiguration,deleteCookie);
            }
            if(optional.isPresent()){
                return optional.get();
            }
        } catch (Exception e) {
            throw new WebEngineException("Error during get driver",e);
        }
        return null;
    }

    protected WebElementDescription populateWebElement(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws  WebEngineException{
        Map.Entry<TargetKey,String> entry = getTargetValue(globalApplicationContext, commandData, commandResultList);
        WebDriver webDriver = getWebDriverToUse(commandResultList);
        if(entry==null){
            return WebElementDescription.builder()
                    .useDriver(webDriver)
                    .build();
        }

        TargetKey targetKey = entry.getKey();
        if(targetKey == TargetKey.EXPRESSION_TO_EVALUATE){
            targetKey = TargetKey.getTargetValueList(commandData.getCommand(), entry.getValue()).getKey();
        }
        switch (targetKey){
            case ID:
                return WebElementDescription.builder()
                        .useDriver(webDriver)
                        .id(entry.getValue())
                        .build();
            case XPATH:
                return WebElementDescription.builder()
                        .useDriver(webDriver)
                        .xPath(entry.getValue())
                        .build();
            case COMBINAISON_OF_LOCATOR:
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                try {
                    WebElementDescription webElementDescription = mapper.readValue(entry.getValue(), WebElementDescription.class);
                    webElementDescription.setUseDriver(webDriver);
                    return webElementDescription;
                } catch (JsonProcessingException e) {
                    throw new WebEngineException("Veuillez vérifier le format de la combinaison de locator : "+entry.getValue(), e);
                }

            default:
                return WebElementDescription.builder()
                        .useDriver(webDriver)
                        .build();
        }
    }

    protected Map.Entry<TargetKey,String> getTargetValue(AbstractGlobalApplicationContext globalApplicationContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        String value;
        Set<TargetKey> targetKeyList = commandData.getTargetList().keySet();
        if (MapUtils.isNotEmpty(commandData.getTargetList()) && targetKeyList.size() == 1) {
            TargetKey targetKey = targetKeyList.iterator().next();
            value = commandData.getTargetList().getOrDefault(targetKey, StringUtils.EMPTY);
            String evaluateValue = StringUtils.isNotEmpty(value) ? EvaluateValueHelper.evaluateValue(globalApplicationContext.getSettings(), value, commandResultList) : StringUtils.EMPTY; //For xpath, id ...etc dynamic
            return MapUtils.isNotEmpty(commandData.getTargetList()) ? new AbstractMap.SimpleEntry(targetKeyList.iterator().next(), evaluateValue) : null;
        }
        return null;
    }

    public CommandResult execute(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        ActionReport actionReport = ActionReportHelper.getActionReport(commandData.getName());
        String dataTestColumName = ((TestCaseNoCodeContext) testCaseContext).getDataTestColumnName();
        loggerService.info("----------------------------------------------------------------------------------------------");
        loggerService.info("Executed command : " + commandData);
        try {
            Map.Entry<TargetKey,String> targetEntry = getTargetValue(globalApplicationContext, commandData, commandResultList);
            String evaluateValue = getValue(globalApplicationContext, (TestCaseNoCodeContext)testCaseContext, commandData, commandResultList);

            getLogReport().getVariables().add(VariableHelper.getVariable("Target", targetEntry == null ? "" : targetEntry.getValue()));
            getLogReport().getVariables().add(VariableHelper.getVariable("Executed command", commandData.getCommand().name()));
            getLogReport().getVariables().add(VariableHelper.getVariable("Data", evaluateValue == null ? "" : evaluateValue));

            if (CommandDataHelper.canExecuteDataTestColumn(commandData.getDataTestReferenceList(), dataTestColumName)) {
                executeCmd(globalApplicationContext, testCaseContext, commandData, commandResultList);
                actionReport.getScreenshots().getScreenshotReports().addAll(getScreenshotReportList());
                actionReport.setResult(Result.PASSED);
                getLogReport().getVariables().add(VariableHelper.getVariable("Status", Result.PASSED.value()));
            } else {
                actionReport.setResult(Result.IGNORED);
                getLogReport().getVariables().add(VariableHelper.getVariable("Warning", "Command ignored because the colum data-test-ref not contains '" + dataTestColumName + "' column "));
            }
            actionReport.setLogMap(getLogReport());
        } catch (Throwable e) {
            if(getWebDriverToUse(commandResultList)!=null){
                actionReport.getScreenshots().getScreenshotReports().add(screenShot(globalApplicationContext,testCaseContext,"",commandResultList));
            }
            if (commandData.isOptional() || commandData.getCommand() == CommandName.IF || commandData.getCommand() == CommandName.ELSE_IF) {
                actionReport.setName(actionReport.getName() + " - /!\\ Failed but ignored (Optional or If/else if/else)");
                actionReport.setResult(Result.IGNORED);
                getLogReport().getVariables().add(VariableHelper.getVariable("Warning", "Command failed but ignored because this command is optional"));
            }else{
                actionReport.setResult(Result.FAILED);
                getLogReport().getVariables().add(VariableHelper.getVariable("Status", Result.FAILED.value()));
            }
            getLogReport().getVariables().add(VariableHelper.getVariable("Cause", e.getMessage()));
            getLogReport().getVariables().add(VariableHelper.getVariable("Exception", ExceptionUtils.getStackTrace(e)));
            actionReport.setLogMap(getLogReport());
        } finally {
            actionReport.setEndTime(Calendar.getInstance());
        }

        loggerService.info("Status command : "+actionReport.getResult());
        return CommandResult.builder()
                .commandData(commandData)
                .actionReport(actionReport)
                .driverContext(driverContext)
                .savedData(savedData).build();
    }

    protected ScreenshotReport screenShot(AbstractGlobalApplicationContext globalApplicationContext,AbstractTestCaseContext testCaseContext,String name, List<CommandResult> commandResultList) throws WebEngineException {
        WebDriver webDriver = getWebDriverToUse(commandResultList);
        if(webDriver==null){
            loggerService.warn("No driver found to take a screenshot. Try to take a screenshot with the desktop image.");
            return fullScreenShot(globalApplicationContext,testCaseContext,name,commandResultList);
        }
        byte[] screenshot = new byte[0];
        try {
            screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            loggerService.warn("Error during screenshot, will use fullscreenshot", e);
            screenshot = getFullScreenShotBytes();
        }
        return ScreenshotHelper.getScreenshotReport(name, screenshot);
    }

    protected ScreenshotReport fullScreenShot(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, String name, List<CommandResult> commandResultList) throws WebEngineException {
        try {
            byte[] screenshot = getFullScreenShotBytes();
            return ScreenshotHelper.getScreenshotReport(name, screenshot);
        } catch (Exception e) {
            loggerService.warn("Error during full screenshot", e);
            throw new WebEngineException("Error during full screenshot", e);
        }
    }

    @NotNull
    private byte[] getFullScreenShotBytes() {
        RenderedImage img = getGeneratedCurrentDesktopImage();
        byte[] screenshot = getImgByteArray(img);
        return screenshot;
    }

    private RenderedImage getGeneratedCurrentDesktopImage() {
        Robot robot = null;
        try {
            robot = new Robot();
            BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            return screenShot;
        } catch (AWTException e) {
            loggerService.warn("Error during get generated current desktop image", e);
        }

        return new BufferedImage(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,Image.SCALE_DEFAULT);
    }

    private byte[] getImgByteArray(RenderedImage img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
        } catch (IOException e) {
            loggerService.warn("Error during get image byte array", e);
        }
        return baos.toByteArray();
    }

    protected WebDriver getWebDriverToUse(List<CommandResult> commandResultList) throws WebEngineException {
        List<DriverContext> driverContextList = CommandResultHelper.getWebDriverList(commandResultList);
        if(CollectionUtils.isNotEmpty(driverContextList)){
            return ListUtil.getLastElement(driverContextList.stream().collect(Collectors.toList())).get().getWebDriver();
        }
        return null;
    }

    protected String getValue(AbstractGlobalApplicationContext globalApplicationContext, TestCaseNoCodeContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        String dataTestColumName = testCaseContext.getDataTestColumnName();
        Map<String, String> dataTestMap = commandData.getDataTestMap();
        if (MapUtils.isNotEmpty(dataTestMap) && StringUtils.isNotEmpty(dataTestMap.get(dataTestColumName))) {
            String originalValue = dataTestMap.get(dataTestColumName);
            return EvaluateValueHelper.evaluateValue(globalApplicationContext.getSettings(), originalValue, commandResultList);
        }
        return null;
    }
}