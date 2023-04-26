package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class CommandFactory {

    public static AbstractDriverCommand getCommand(CommandDataDriveByExcel commandData) throws IllegalArgumentException {
        switch (commandData.getCommand()){
            case OPEN:
                return new OpenCommand();
            case SEND_KEYS:
                return new SendKeysCommand();
            case CLICK:
                return new ClickCommand();
            case SAVE_DATA:
                return new SaveDataCommand();
            case SELECT:
                return new SelectCommand();
            case ASSERT_EXIST:
                return new AssertExistCommand();
            case ASSERT_NOT_EXIST:
                return new AssertNotExistCommand();
            case ASSERT_SELECTED:
                return new AssertSelectedCommand();
            case ASSERT_NOT_SELECTED:
                return new AssertNotSelectedCommand();
            case ASSERT_CHECKED:
                return new AssertCheckedCommand();
            case ASSERT_NOT_CHECKED:
                return new AssertNotCheckedCommand();
            case SCREENSHOT:
                return new ScrenshotCommand();
            case ASSERT_CONTENT:
                return new AssertContentCommand();
            case ASSERT_NOT_CONTENT:
                return new AssertNotContentCommand();
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
