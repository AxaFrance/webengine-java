package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import org.openqa.selenium.WebElement;

import java.util.Map;

public class AssertContentCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, Map<String, CommandResult> commandResultMap)throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        WebElement webElement = webElementDescription.findElement();
        String value = getValue((TestCaseDriveByExcelContext) testCaseContext, commandData, commandResultMap);
        if(!StringUtil.equalsIgnoreCase(webElementDescription.getText(),value)){
            throw new WebEngineException("The specified value don't match with the content of the element");
        }
    }
}
