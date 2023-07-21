package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.ActionReportHelper;
import fr.axa.automation.webengine.helper.CommandDataHelper;
import fr.axa.automation.webengine.helper.CommandResultHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;

public class CallCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        return;
    }

    @Override
    public CommandResult execute(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws WebEngineException {
        CommandResult commandResult;
        ActionReport actionReport;
        String dataTestColumName = ((TestCaseNoCodeContext)testCaseContext).getDataTestColumnName();
        if (CommandDataHelper.canExecuteDataTestColumn(commandData.getDataTestReferenceList(), dataTestColumName)) {
            commandResult = CommandResultHelper.getCommandResult(commandData, ActionReportHelper.getActionReport(commandData.getName(), Result.PASSED), "");
        } else {
            actionReport = ActionReportHelper.getActionReport(commandData.getName(), Result.IGNORED);
            actionReport.setLog("Command ignored because the colum data-test-ref not contains '" + dataTestColumName + "' column");
            commandResult = CommandResultHelper.getCommandResult(commandData, actionReport, "");
        }
        return commandResult;
    }
}
