package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CheckByValueCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String value = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        executeActionInElement(StringUtils.trimToEmpty(value));
    }

    protected void executeActionInElement(String value)throws Exception {
        if(StringUtils.isEmpty(value)){
           throw new Exception("The value is empty, we can't determine the checkbox or radio to click");
        }
        webElementDescription.checkByValue(value);
    }
}
