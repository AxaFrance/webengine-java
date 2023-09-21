package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.object.CommandDataNoCode;
import org.apache.commons.lang3.StringUtils;

public class CommandNameHelper {

    public static String getCommandName(CommandDataNoCode commandData) {
        String name = commandData.getName();
        if(StringUtils.isEmpty(name)){
            name = commandData.getCommand().name();
        }
        return name;

    }
}
