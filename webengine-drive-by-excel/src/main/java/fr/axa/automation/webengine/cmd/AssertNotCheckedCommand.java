package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.HtmlAttributeConstante;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;

public class AssertNotCheckedCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        boolean isChecked = webElementDescription.assertContentByElementType(HtmlAttributeConstante.ATTRIBUTE_CHECKED.getValue());
        if(isChecked){
            throw  new WebEngineException("The element is checked");
        }
    }
}
