package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class CommandFactory {

    public static AbstractCommand getCommand(CommandDataDriveByExcel commandData) throws IllegalArgumentException {
        switch (CommandName.fromValue(commandData.getCommand())){
            case OPEN:
                return new OpenCommand();
            case SEND_KEY:
                return new SendKeysCommand();
            case CLICK:
                return new ClickCommand();
            default:
                throw new IllegalArgumentException("Command not recognized");
        }
    }

}
