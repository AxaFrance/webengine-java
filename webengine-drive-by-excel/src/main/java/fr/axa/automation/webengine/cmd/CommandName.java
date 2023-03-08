package fr.axa.automation.webengine.cmd;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum CommandName {
    END_SCENARII("end scenarii"),IF("if"),END_IF("end if"),CALL("call");
    final String name;
}
