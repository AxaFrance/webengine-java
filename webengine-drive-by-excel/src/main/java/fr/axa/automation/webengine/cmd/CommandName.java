package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.util.StringUtil;
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

    IF("if"), ELSE_IF("else if"),ELSE("else"),END_IF("end if"),

    SAVE_DATA("save data"),

    ASSERT_EXIST("assert exist"),

    ASSERT_SELECTED("assert selected"),

    ASSERT_CONTENT("assert content"),

    ASSERT_CHECKED("assert checked"),

    SCREENSHOT("screenshot"),

    WAIT("wait"),
    END_SCENARIO("end scenario");

    final String commandLibelle;


    public static CommandName fromValue(String value) {
        for (CommandName commandName: CommandName.values()) {
            if (StringUtil.equalsIgnoreCase(value,commandName.getCommandLibelle())) {
                return commandName;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
