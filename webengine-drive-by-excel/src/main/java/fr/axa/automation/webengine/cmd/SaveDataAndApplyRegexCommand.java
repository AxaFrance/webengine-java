package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.List;
import java.util.Optional;

public class SaveDataAndApplyRegexCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        if(MapUtils.isEmpty(commandData.getTargetList())){
            throw new Exception("No target found for command save and apply regex");
        }
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String regex = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        String textInElement = webElementDescription.getTextByElement();
        getLogReport().getVariables().add(VariableHelper.getVariable("Value in element", textInElement));
        Optional textInElementWithRegexOptional = RegexUtil.findFirst(regex,textInElement);
        String textInElementAfterApplyRegex = textInElementWithRegexOptional.isPresent() ? textInElementWithRegexOptional.get().toString() : "";
        getLogReport().getVariables().add(VariableHelper.getVariable("Value in element after apply regex", textInElementAfterApplyRegex));
        setSavedData(textInElementAfterApplyRegex);
    }
}
