package fr.axa.automation.webengine.cmd;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum CommandName {

    OPEN("open"),
    SEND_KEY("sendKeys"), CLICK("click"),SELECT("select"),
    CALL("call"),

    STORE("store"),
    IF("if"), ELSE_IF("else if"),ELSE("else"),END_IF("end if"),

    SAVE_DATA("saveData"),

    IS_EXIST("isExist"),

    IS_SELECTED("isSelected"),

    ASSERT_CONTENT("asserContent"),

    SCREENSHOT("screenshot"),

    WAIT("wait"),
    END_SCENARII("endScenarii");

    final String name;


    public static CommandName fromValue(String v) {
        for (CommandName commandName: CommandName.values()) {
            if (commandName.name.equals(v)) {
                return commandName;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
