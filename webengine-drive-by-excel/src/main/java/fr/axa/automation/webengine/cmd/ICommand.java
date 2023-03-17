package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public interface ICommand {
    Object execute(CommandDataDriveByExcel commandData) throws Exception;
}
