package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class CommandNameHelper {

    public static String getCommandName(CommandDataDriveByExcel commandData) {
        return commandData.getName();
    }
}
