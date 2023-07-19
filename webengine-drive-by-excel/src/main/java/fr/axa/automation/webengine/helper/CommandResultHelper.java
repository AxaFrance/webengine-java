package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.generated.Result;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.WebDriver;

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

    public static List<CommandResult>  getCommand(List<CommandResult> commandResultList, CommandName commandName){
        return commandResultList
                .stream()
                .filter(commandResult -> commandResult.getCommandData().getCommand()==commandName)
                .collect(Collectors.toList());
    }

    public static List<WebDriver> getWebDriverByOpenCommand(List<CommandResult> commandResultList) {
        List<CommandResult> commandResultOpenList = CommandResultHelper.getCommand(commandResultList,CommandName.OPEN);
        if(CollectionUtils.isEmpty(commandResultOpenList)){
            commandResultOpenList = CommandResultHelper.getCommand(commandResultList,CommandName.OPEN_PRIVATE);;
        }

        if(CollectionUtils.isEmpty(commandResultOpenList)){
            for ( CommandResult commandResult : commandResultList ) {
                 if(CollectionUtils.isNotEmpty(commandResult.getSubCommandResultList())){
                     return  getWebDriverByOpenCommand(commandResult.getSubCommandResultList());
                 }
            }
        }
        return commandResultOpenList.stream().map(commandResult -> commandResult.getWebDriver()).collect(Collectors.toList());
    }
}
