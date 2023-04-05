package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class CommandFactory {

    public static AbstractDriverCommand getCommand(CommandDataDriveByExcel commandData) throws IllegalArgumentException {
        switch (commandData.getCommand()){
            case OPEN:
                return new OpenCommand();
            case SEND_KEY:
                return new SendKeysCommand();
            case CLICK:
                return new ClickCommand();
            case SAVE_DATA:
                return new SaveDataCommand();
            case WAIT:
                return new WaitCommand();
            default:
                throw new IllegalArgumentException("Command not recognized");
        }
    }
}
