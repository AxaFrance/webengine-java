package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandDataNoCode;
import org.apache.commons.lang3.StringUtils;

public class SendKeysWithoutAssertionCommand extends SendKeysCommand{

    @Override
    protected void executeActionInElement(String value, CommandDataNoCode cmdData)throws Exception {
        if(value.startsWith("KEY_")){
            webElementDescription.sendKeyboard(StringUtils.substringAfterLast(value,"KEY_"));
        }else{
            webElementDescription.sendKeysWithoutAssertion(value);
        }
    }
}
