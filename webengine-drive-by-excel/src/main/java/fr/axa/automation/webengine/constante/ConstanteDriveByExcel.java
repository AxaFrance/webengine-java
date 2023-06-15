package fr.axa.automation.webengine.constante;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ConstanteDriveByExcel {
    BRACKETS_PREFIX("#{"),
    BRACKETS_SUFFIX("}#"),
    TRIPLE_CHEVRON_PREFIX("<<<"),
    TRIPLE_CHEVRON_SUFFIX(">>>"),
    SEMICOLON(";"),
    EXCLAMATION_MARK("!"),
    MINUS("-"),
    PLUS("+"),
    DASH("-"),
    COMMAND_FILE_NAME("command.yml"),
    CR_LF ("\r\n");
    final String value;
}
