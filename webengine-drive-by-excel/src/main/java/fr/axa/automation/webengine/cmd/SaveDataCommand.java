package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.EvaluateValueHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.List;

public class SaveDataCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String dataTestColumName = ((TestCaseNoCodeContext)testCaseContext).getDataTestColumnName();
        String dataToSave = "";
        if(MapUtils.isEmpty(commandData.getTargetList())){
            String value = commandData.getDataTestMap().get(dataTestColumName);
            List<String> integrationRegexValueList = RegexUtil.match(RegexContante.INTEGRATION_REGEX_VALUE, value);
            if(CollectionUtils.isNotEmpty(integrationRegexValueList)){
                dataToSave = EvaluateValueHelper.evaluateIntegrationRegexValue(value,integrationRegexValueList,globalApplicationContext.getSettings());
            }else{
                dataToSave = value;
            }
        }else{
            dataToSave = webElementDescription.getTextByElement();
        }
//        String dataToSave = MapUtils.isEmpty(commandData.getTargetList()) ? commandData.getDataTestMap().get(dataTestColumName) : webElementDescription.getTextByElement();
        setSavedData(dataToSave);
    }
}
