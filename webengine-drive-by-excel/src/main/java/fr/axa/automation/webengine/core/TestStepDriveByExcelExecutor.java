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
import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
@Component
@Qualifier("testStepDriveByExcelExecutor")
public class TestStepDriveByExcelExecutor extends AbstractTestStepExecutor implements ITestStepDriveByExcelExecutor {

    public TestStepDriveByExcelExecutor() {
        super();
    }

    @Override
    public ActionReport run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws WebEngineException {
        ActionReport actionReport = ActionReportHelper.getActionReport(commandData.getId());
        try {
            actionReport = (ActionReport)executeCmd(globalApplicationContext,testCaseContext,commandData);
        } catch (Throwable throwable){
            actionReport.setResult(Result.FAILED);
            actionReport.getScreenshots().getScreenshotReports().add(screenShot(globalApplicationContext,testCaseContext,commandData));
            actionReport.setLog("Command "+commandData.getId()+" Failed");
        }finally {
            actionReport.setEndTime(Calendar.getInstance());
        }
        return actionReport;
    }

    private ScreenshotReport screenShot(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws WebEngineException {
        ScrenshotCommand screnshotCommand = new ScrenshotCommand();
        return (ScreenshotReport) screnshotCommand.execute(globalApplicationContext,testCaseContext,commandData);
    }

    @Async("threadPoolTaskExecutor")
    public Object executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws WebEngineException {
        AbstractDriverCommand command = CommandFactory.getCommand(commandData);
        return command.execute(globalApplicationContext,testCaseContext,commandData);
    }
}
