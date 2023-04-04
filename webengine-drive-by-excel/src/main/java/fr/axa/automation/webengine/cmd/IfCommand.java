package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.Map;

public class IfCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, Map<String, CommandResult> commandResultMap)throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        String dataTestColumName = ((TestCaseDriveByExcelContext)testCaseContext).getDataTestColumnName();
        String dataTest = commandData.getDataTestMap().get(dataTestColumName);
        if(!containsValue(webElementDescription,dataTest) || !isPresent(webElementDescription,dataTest)){
            throw new WebEngineException("If command failed");
        }
    }

    private boolean containsValue(WebElementDescription webElementDescription, String value) throws Exception {
      return webElementDescription.getInnerHtml().contains(value);
    }

    private boolean isPresent(WebElementDescription webElementDescription, String value) throws Exception {
        return webElementDescription.findElement()!=null;
    }



}
