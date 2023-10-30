package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.ConstantNoCode;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.ElementContentForInputSelect;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
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
        ElementContentForInputSelect elementContentForInputSelect = (ElementContentForInputSelect)webElementDescription.getSelectedOption();
        if(!assertion(elementContentForInputSelect,expectedValue)){
            String errorMessage = "The expected value is : '" + expectedValue + "'";
            getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(errorMessage);
            errorMessage = "The actual selected option is : '" + elementContentForInputSelect.getValueAndTextMap().entrySet().stream().findFirst().get() + "'";
            getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(errorMessage);
            throw new WebEngineException(errorMessage);
        }
    }

    protected boolean assertion(ElementContentForInputSelect elementContentForInputSelect, String expected){
        if(elementContentForInputSelect != null && MapUtils.isNotEmpty(elementContentForInputSelect.getValueAndTextMap())){
            String actualValue = elementContentForInputSelect.getValueAndTextMap().entrySet().stream().findFirst().get().getKey();
            if(StringUtil.equalsIgnoreCase(expected, actualValue)){
                return true;
            }
            String actualText = elementContentForInputSelect.getValueAndTextMap().entrySet().stream().findFirst().get().getValue();
            if(actualText.contains(expected)){
                return true;
            }
        }
        return false;
    }
}
