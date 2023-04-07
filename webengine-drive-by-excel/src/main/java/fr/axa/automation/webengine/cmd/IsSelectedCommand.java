package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.Map;

public class IsSelectedCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, Map<String, CommandResult> commandResultMap)throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        if(!webElementDescription.isSelected()){
            throw  new WebEngineException("The element is not selected");
        }
    }
}
