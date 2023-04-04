package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestStepDriveByExcelExecutor;
import fr.axa.automation.webengine.cmd.AbstractDriverCommand;
import fr.axa.automation.webengine.cmd.CommandFactory;
import fr.axa.automation.webengine.cmd.ScrenshotCommand;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
@Component
@Qualifier("testStepDriveByExcelExecutor")
public class TestStepDriveByExcelExecutor extends AbstractTestStepExecutor implements ITestStepDriveByExcelExecutor {

    public TestStepDriveByExcelExecutor() {
        super();
    }

    @Override
    public CommandResult run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, Map<String, CommandResult> commandResultMap) throws WebEngineException {
        CommandResult commandResult = null;
        ActionReport actionReport = ActionReportHelper.getActionReport(commandData.getName());

        try {
            commandResult = executeCmd(globalApplicationContext,testCaseContext,commandData,commandResultMap);
        } catch (Throwable throwable){
            actionReport.setResult(Result.FAILED);
            actionReport.getScreenshots().getScreenshotReports().add(screenShot(globalApplicationContext,testCaseContext,commandData,commandResultMap));
            actionReport.setLog("Command "+commandData.getName()+" Failed");
        }finally {
            actionReport.setEndTime(Calendar.getInstance());
        }
        return CommandResult.builder().actionReport(commandResult !=null ? commandResult.getActionReport() : actionReport)
                                        .savedData(commandResult !=null ? commandResult.getSavedData() : "")
                                        .build();
    }

    private ScreenshotReport screenShot(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, Map<String, CommandResult> commandResultMap) throws WebEngineException {
        ScrenshotCommand screnshotCommand = new ScrenshotCommand();
        ActionReport actionReport = screnshotCommand.execute(globalApplicationContext,testCaseContext,commandData,commandResultMap).getActionReport();
        return actionReport.getScreenshots().getScreenshotReports().get(0);
    }

    @Async("threadPoolTaskExecutor")
    public CommandResult executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData,  Map<String, CommandResult> commandResultMap) throws WebEngineException {
        AbstractDriverCommand command = CommandFactory.getCommand(commandData);
        return command.execute(globalApplicationContext,testCaseContext,commandData,commandResultMap);
    }
}
