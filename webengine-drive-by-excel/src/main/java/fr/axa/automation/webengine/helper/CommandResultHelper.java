package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;
import java.util.stream.Collectors;

public class CommandResultHelper {

    public static CommandResult getCommandResult(CommandDataNoCode commandData, ActionReport actionReport, String data){
        return CommandResult.builder().commandData(commandData).actionReport(actionReport).savedData(data).build();
    }

    public static List<ActionReport> getActionReportList(List<CommandResult> commandResultList){
        return commandResultList.stream().map(commandResult -> commandResult.getActionReport()).collect(Collectors.toList());
    }

    public static boolean isResultExpected(CommandResult commandResult, Result result){
        return commandResult.getActionReport().getResult()==result;
    }
}
