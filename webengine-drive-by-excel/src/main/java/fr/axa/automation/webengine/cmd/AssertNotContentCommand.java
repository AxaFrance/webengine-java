package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.List;

public class AssertNotContentCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(testCaseContext,commandData,commandResultList);
        String value = getValue((TestCaseDriveByExcelContext) testCaseContext, commandData, commandResultList);
        boolean isContentValue = webElementDescription.assertContentByElementType(value);
        if(isContentValue){
            throw  new WebEngineException("The element content the value : "+value);
        }
    }
}
