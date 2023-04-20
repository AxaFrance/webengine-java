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
            case SELECT:
                return new SelectCommand();
            case IS_EXIST:
                return new IsExistCommand();
            case IS_SELECTED:
                return new IsSelectedCommand();
            case ASSERT_CHECKED:
                return new AssertCheckedCommand();
            case SCREENSHOT:
                return new ScrenshotCommand();
            case ASSERT_CONTENT:
                return new AssertContentCommand();
            case IF:
                return new IfCommand();
            case ELSE_IF:
                return new ElseIfCommand();
            case ELSE:
                return new ElseCommand();
            case WAIT:
                return new WaitCommand();
            default:
                throw new IllegalArgumentException("Command not recognized");
        }
    }
}
