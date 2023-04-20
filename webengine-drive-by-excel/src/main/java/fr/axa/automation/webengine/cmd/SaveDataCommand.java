package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.collections4.MapUtils;

import java.util.List;

public class SaveDataCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(commandData,testCaseContext);
        String dataTestColumName = ((TestCaseDriveByExcelContext)testCaseContext).getDataTestColumnName();
        String dataToSave = MapUtils.isEmpty(commandData.getTargetList()) ? commandData.getDataTestMap().get(dataTestColumName) : webElementDescription.getTextByElement();
        setSavedData(dataToSave);
    }
}
