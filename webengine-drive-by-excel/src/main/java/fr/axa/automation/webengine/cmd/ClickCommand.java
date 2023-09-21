package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ClickCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String value = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        executeActionInElement(StringUtils.trimToEmpty(value));
    }

    protected void executeActionInElement(String value)throws Exception {
        if(StringUtils.isEmpty(value)){
            try {
                webElementDescription.focusAndClick();
            }catch (Exception e) {
                webElementDescription.focusAndClickWithJS();
            }
        } else if (webElementDescription.isInputRadio()) {
            webElementDescription.checkByValue(value);
        }
    }
}