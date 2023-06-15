package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;

import java.util.List;

public class AssertNotSelectedCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String value = getValue(globalApplicationContext,(TestCaseDriveByExcelContext) testCaseContext, commandData, commandResultList);
        String selectedOption = webElementDescription.getSelectedOption(value);
        if(StringUtil.equalsIgnoreCase(value,selectedOption)){
            String errorMessage = "The value is : "+value+" and the selected option is : "+selectedOption;
            getLogReport().append(errorMessage);
            throw  new WebEngineException(errorMessage);
        }
    }
}
