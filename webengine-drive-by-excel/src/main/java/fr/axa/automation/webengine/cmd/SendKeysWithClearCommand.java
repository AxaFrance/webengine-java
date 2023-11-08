package fr.axa.automation.webengine.cmd;

import org.apache.commons.lang3.StringUtils;

public class SendKeysWithClearCommand extends SendKeysCommand{

    protected void executeActionInElement(String value)throws Exception {
        if(value.startsWith("KEY_")){
            webElementDescription.sendKeyboard(StringUtils.substringAfterLast(value,"KEY_"));
        }else{
            webElementDescription.sendKeysWithClearBefore(value);
        }
    }
}
