package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.ConstantNoCode;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.ElementContent;
import fr.axa.automation.webengine.global.ElementContentForInputSelect;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssertNotContentCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String expectedValue = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        ElementContent elementContent = webElementDescription.getContentByElementType();
        String result = null;

        if(elementContent instanceof ElementContentForInputSelect){
            Map<String, String> actualValueMap = ((ElementContentForInputSelect)elementContent).getValueAndTextMap()
                    .entrySet().stream()
                    .filter(entry -> StringUtil.equalsIgnoreCase(expectedValue,entry.getValue()) || StringUtil.equalsIgnoreCase(expectedValue,entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (MapUtils.isNotEmpty(actualValueMap)) {
                result = ((ElementContentForInputSelect)elementContent).getValueAndTextMap().toString();
            }
        }else{
            String actualValue = elementContent.getValue();
            if(StringUtil.equalsIgnoreCase(expectedValue,actualValue)){
                result = actualValue;
            }
        }

        if(StringUtils.isNotEmpty(result)){
            String errorMessage = "The expected value is : '" + expectedValue + "'";
            getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(errorMessage);
            errorMessage = "The actual value is : '" + result + "'";
            getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(errorMessage);
            throw new WebEngineException(errorMessage);
        }
    }
}
