package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandDataNoCode;

public class CommandFactory {

    public static AbstractDriverCommand getCommand(CommandDataNoCode commandData) throws IllegalArgumentException {
        switch (commandData.getCommand()){
            case OPEN:
                return new OpenCommand();
            case OPEN_PRIVATE:
                return new OpenInPrivateModeCommand();
            case CLEAR:
                return new ClearCommand();
            case SEND_KEYS:
                return new SendKeysCommand();
            case SEND_KEYS_WITH_CLEAR:
                return new SendKeysWithClearCommand();
            case CLICK:
                return new ClickCommand();
            case SAVE_DATA:
                return new SaveDataCommand();
            case CALL:
                return new CallCommand();
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
            case POPUP:
                return new PopUpCommand();
            case UPLOADFILE:
                return new UploadFileCommand();
            case SWITCH_FRAME:
                return new SwitchFrameCommand();
            case EXIT_FRAME:
                return new ExitFrameCommand();
            case REFRESH:
                return new RefreshCommand();
            default:
                throw new IllegalArgumentException("Command not recognized");
        }
    }
}
