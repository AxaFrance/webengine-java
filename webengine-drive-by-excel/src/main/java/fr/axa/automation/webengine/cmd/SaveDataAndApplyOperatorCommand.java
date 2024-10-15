package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.OperatorAndOperandEvaluatorHelper;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.collections4.MapUtils;

import java.util.List;

public class SaveDataAndApplyOperatorCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        if(MapUtils.isEmpty(commandData.getTargetList())){
            throw new Exception("No target found for command save and apply regex");
        }
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String originalValue = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        String evaluatedValue = String.valueOf(OperatorAndOperandEvaluatorHelper.evaluate(originalValue));
        getLogReport().getVariables().add(VariableHelper.getVariable("Original value", originalValue));
        getLogReport().getVariables().add(VariableHelper.getVariable("Value after apply operator", evaluatedValue));
        setSavedData(evaluatedValue);
    }
}
