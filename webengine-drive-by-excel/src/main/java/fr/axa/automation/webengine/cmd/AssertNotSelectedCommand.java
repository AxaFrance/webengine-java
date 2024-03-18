package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.ElementContentSelect;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;

public class AssertNotSelectedCommand extends AssertSelectedCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String expectedValue = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        ElementContentSelect elementContentSelect = (ElementContentSelect)webElementDescription.getSelectedOption();
        if(assertion(elementContentSelect,expectedValue)){
            String errorMessage = "The expected value is : '" + expectedValue + "' The actual selected option is : '" + elementContentSelect.getValueAndTextMap().entrySet().stream().findFirst().get() + "'";
            getLogReport().getVariables().add(VariableHelper.getVariable("Expected value", expectedValue));
            getLogReport().getVariables().add(VariableHelper.getVariable("Actual value", String.valueOf(elementContentSelect.getValueAndTextMap().entrySet().stream().findFirst().get())));
            throw new WebEngineException(errorMessage);
        }
    }
}
