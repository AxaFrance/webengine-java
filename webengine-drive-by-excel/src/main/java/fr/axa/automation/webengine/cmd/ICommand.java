package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.SettingsDriveByExcel;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public interface ICommand {
    Object execute(CommandDataDriveByExcel commandData, SettingsDriveByExcel settingsDriveByExcel)throws Exception;
}
