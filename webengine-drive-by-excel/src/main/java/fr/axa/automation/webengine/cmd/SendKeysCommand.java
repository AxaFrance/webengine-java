package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.helper.EvaluateValueHelper;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;

import java.util.Map;

public class SendKeysCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, Map<String, CommandResult> commandResultMap)throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        String value = getValue((TestCaseDriveByExcelContext) testCaseContext, commandData, commandResultMap);
        executeActionForElement(value);
    }

    protected String getValue(TestCaseDriveByExcelContext testCaseContext, CommandDataDriveByExcel commandData, Map<String, CommandResult> commandResultMap) {
        String dataTestColumName = testCaseContext.getDataTestColumnName();
        String originalValue = commandData.getDataTestList().get(dataTestColumName);
       return EvaluateValueHelper.evaluateValue(originalValue, commandResultMap);
    }

    protected void executeActionForElement(String value)throws Exception {
        if(webElementDescription.isSelect()){
            selectByValue(value);
        } else if (webElementDescription.isInputRadio()) {
            selectByValueForInputRadio(value);
        } else if (webElementDescription.isInputCheckbox()) {
            webElementDescription.click();
        }else{
            webElementDescription.sendKeys(value);
        }
    }

    protected void selectByValueForInputRadio(String value) throws Exception {
        webElementDescription.checkByValue(value);
    }

    protected void selectByValue(String value) throws Exception {
        try{
            webElementDescription.selectByValue(value);
        }catch (Exception ex){
            selectByText(value);
        }
    }

    protected void selectByText(String value) throws Exception {
        webElementDescription.selectByText(value);
    }

}
