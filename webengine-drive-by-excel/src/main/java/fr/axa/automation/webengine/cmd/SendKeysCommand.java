package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.helper.DateValueByTag;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class SendKeysCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        String dataTestColumName = ((TestCaseDriveByExcelContext)testCaseContext).getDataTestColumnName();
        String value = commandData.getDataTestList().get(dataTestColumName);

//        if(DateValueByTag.isTagDateValue(value)){
//            value = DateValueByTag.getTagValue(value);
//        } else if () {
//
//        }

        webElementDescription.sendKeys(value);
    }



}
