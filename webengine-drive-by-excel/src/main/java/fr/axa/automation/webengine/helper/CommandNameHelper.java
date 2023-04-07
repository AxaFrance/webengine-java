package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class CommandNameHelper {

    public static String getCommandName(CommandDataDriveByExcel commandData) {
        if(StringUtils.isNotEmpty(commandData.getName()) && (CommandName.SAVE_DATA == commandData.getCommand())){
            return StringUtil.removeSpecialCharacters(commandData.getName());
        }
        return  commandData.getUid();
    }
}
