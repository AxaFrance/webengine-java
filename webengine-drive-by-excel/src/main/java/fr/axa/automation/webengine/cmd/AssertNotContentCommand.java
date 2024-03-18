package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.AssertContentResult;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;

public class AssertNotContentCommand extends AbstractDriverCommand{
    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext, testCaseContext, commandData, commandResultList);
        String expectedValue = getValue(globalApplicationContext, (TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        AssertContentResult assertContentResult = webElementDescription.assertContentByElementType(expectedValue);
        if(assertContentResult.isResult()){
            String errorMessage = "The expected value is : '" + expectedValue + "' and the actual value is : '" + assertContentResult.getActualValue() + "'";
            getLogReport().getVariables().add(VariableHelper.getVariable("Expected value", expectedValue));
            getLogReport().getVariables().add(VariableHelper.getVariable("Actual value", assertContentResult.getActualValue()));
            throw new WebEngineException(errorMessage);
        }
    }
}
