package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class SendKeysCommand extends AbstractDriverCommand{

    @Override
    public Object executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        String dataTestColumName = ((TestCaseDriveByExcelContext)testCaseContext).getDataTestColumnName();
        webElementDescription.sendKeys(commandData.getDataTestList().get(dataTestColumName));
        return null;
    }



}
