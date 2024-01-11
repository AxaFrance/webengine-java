package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;

public class ClickPseudoElementCommand extends ClickCommand {

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        webElementDescription = populateWebElement(globalApplicationContext, testCaseContext, commandData, commandResultList);
        executeActionInElement();
    }

    @Override
    protected void executeActionInElement() throws Exception {
        if(webElementDescription.getPseudoElement() != null){
            webElementDescription.focusAndClickOnPseudoElement();
        }else {
            throw new WebEngineException("Pseudo element is null");
        }

    }
}
