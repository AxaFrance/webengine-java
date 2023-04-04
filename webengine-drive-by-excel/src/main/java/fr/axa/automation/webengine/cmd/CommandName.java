package fr.axa.automation.webengine.cmd;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum CommandName {
    CALL("call"),
    OPEN("open"),
    SEND_KEY("sendkeys"), CLICK("click"),

    STORE("store"),
    IF("if"), ELSE_IF("else if"),ELSE("else"),END_IF("end if"),

    SAVE_DATA("saveData"),

    WAIT("wait"),
    END_SCENARII("end scenarii");

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
