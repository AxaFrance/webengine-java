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
    SEND_KEY("send keys"), CLICK("click"),SELECT("select"),
    CALL("call"),

    STORE("store"),
    IF("if"), ELSE_IF("else if"),ELSE("else"),END_IF("end if"),

    SAVE_DATA("save data"),

    IS_EXIST("assert exist"),

    IS_SELECTED("assert selected"),

    ASSERT_CONTENT("assert content"),

    ASSERT_CHECKED("assert checked"),

    SCREENSHOT("screenshot"),

    WAIT("wait"),
    END_SCENARIO("end scenario");

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
