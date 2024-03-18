package fr.axa.automation.webengine.executor;

import fr.axa.automation.webengine.cmd.AbstractDriverCommand;
import fr.axa.automation.webengine.cmd.CommandFactory;
import fr.axa.automation.webengine.cmd.ScrenshotCommand;
import fr.axa.automation.webengine.core.AbstractTestStepExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
@Component
@Qualifier("testStepNoCodeExecutor")
public class TestStepNoCodeExecutor extends AbstractTestStepExecutor implements ITestStepNoCodeExecutor {

    public TestStepNoCodeExecutor() {
        super();
    }

    @Override
    public CommandResult run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        CommandResult commandResult = null;
        ActionReport actionReport = ActionReportHelper.getActionReport(commandData.getName());

        try {
            commandResult = executeCmd(globalApplicationContext,testCaseContext,commandData,commandResultList);
        } catch (Throwable throwable){
            actionReport.setResult(Result.FAILED);
            actionReport.getScreenshots().getScreenshotReports().add(screenShot(globalApplicationContext,testCaseContext,commandData,commandResultList));
            actionReport.setLog("Command " + commandData + " Failed");
        }finally {
            actionReport.setEndTime(Calendar.getInstance());
        }
        return CommandResult.builder().commandData(commandData)
                                        .actionReport(commandResult !=null ? commandResult.getActionReport() : actionReport)
                                        .driverContext(commandResult !=null ? commandResult.getDriverContext() : null)
                                        .savedData(commandResult !=null ? commandResult.getSavedData() : "")
                                        .build();
    }

    private ScreenshotReport screenShot(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        ScrenshotCommand screnshotCommand = new ScrenshotCommand();
        ActionReport actionReport = screnshotCommand.execute(globalApplicationContext,testCaseContext,commandData,commandResultList).getActionReport();
        return actionReport.getScreenshots().getScreenshotReports().get(0);
    }

    @Async("threadPoolTaskExecutor")
    public CommandResult executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        AbstractDriverCommand command = CommandFactory.getCommand(commandData);
        return command.execute(globalApplicationContext,testCaseContext,commandData,commandResultList);
    }
}
