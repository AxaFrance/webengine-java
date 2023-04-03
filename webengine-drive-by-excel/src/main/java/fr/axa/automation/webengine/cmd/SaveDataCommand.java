package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import org.apache.commons.collections4.MapUtils;

public class SaveDataCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws Exception {
        webElementDescription = populateWebElement(commandData,testCaseContext);
        String dataToSave;
        dataToSave = MapUtils.isEmpty(commandData.getTargetList()) ? ((TestCaseDriveByExcelContext)testCaseContext).getDataTestColumnName() : webElementDescription.getInnerText();
        setSavedData(dataToSave);
    }
}
