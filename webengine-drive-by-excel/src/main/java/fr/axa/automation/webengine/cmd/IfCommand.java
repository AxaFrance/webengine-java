package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import java.util.List;

public class IfCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(commandData,testCaseContext);
        WebElement webElement = webElementDescription.findElement();
        String value = getValue((TestCaseDriveByExcelContext) testCaseContext, commandData, commandResultList);
        if(StringUtils.isEmpty(value) && webElementDescription.isNotExists()){
            throw  new WebEngineException("The element doesn't exist");
        }else if (StringUtils.isNotEmpty(value)){
            webElementDescription.assertContentByElementType(webElement,value);
        }
    }



}
