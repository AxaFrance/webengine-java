package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.ElementContentSelect;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.List;

public class AssertSelectedCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String expectedValue = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        ElementContentSelect elementContentSelect = (ElementContentSelect)webElementDescription.getSelectedOption();
        if(!assertion(elementContentSelect,expectedValue)){
            String errorMessage = "The expected value is : '" + expectedValue + "' and the actual value is : '" + elementContentSelect.getValueAndTextMap().entrySet().stream().findFirst().get() + "'";
            getLogReport().getVariables().add(VariableHelper.getVariable("Expected value", expectedValue));
            getLogReport().getVariables().add(VariableHelper.getVariable("Actual value", String.valueOf(elementContentSelect.getValueAndTextMap().entrySet().stream().findFirst().get())));
            throw new WebEngineException(errorMessage);
        }
    }

    protected boolean assertion(ElementContentSelect elementContentSelect, String expected){
        if(elementContentSelect != null && MapUtils.isNotEmpty(elementContentSelect.getValueAndTextMap())){
            String actualValue = elementContentSelect.getValueAndTextMap().entrySet().stream().findFirst().get().getKey();
            if(StringUtil.equalsIgnoreCase(expected, actualValue)){
                return true;
            }
            String actualText = elementContentSelect.getValueAndTextMap().entrySet().stream().findFirst().get().getValue();
            if(actualText.contains(expected)){
                return true;
            }
        }
        return false;
    }
}
