package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class CommandNameHelper {

    public static String getCommandName(CommandDataDriveByExcel commandData) {
        return StringUtils.isEmpty(commandData.getName()) ? commandData.getUid() : StringUtil.removeSpecialCharacters(commandData.getName());
    }
}
