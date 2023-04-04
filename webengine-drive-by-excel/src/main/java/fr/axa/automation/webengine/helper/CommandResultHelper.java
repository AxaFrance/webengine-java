package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandResultHelper {

    public static CommandResult getCommandResult(ActionReport actionReport, String data){
        return CommandResult.builder().actionReport(actionReport).savedData(data).build();
    }

    public static List<ActionReport> getActionReportList(Map<String, CommandResult> commandResultMap){
        return commandResultMap.entrySet().stream().map(entry -> entry.getValue().getActionReport()).collect(Collectors.toList());
    }
}
