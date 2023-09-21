package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.helper.CommandDataHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IfChecking extends AbstractChecking{
    @Override
    public boolean check(AbstractGlobalApplicationContext globalApplicationContext,TestSuiteDataNoCode testSuiteData) {
        List<TestCaseDataNoCode> testCaseDataList = testSuiteData.getTestCaseList();
        Map<String,Boolean> consistencyOfIfAndEndIfCommandMap = new HashMap<>();
        for(TestCaseDataNoCode testCaseData : testCaseDataList){
            consistencyOfIfAndEndIfCommandMap.put(testCaseData.getName(), checkConsistencyCommand(testCaseData));
        }
        assertCommand(consistencyOfIfAndEndIfCommandMap);
        return checkNext(globalApplicationContext,testSuiteData);
    }

    private boolean checkConsistencyCommand(TestCaseDataNoCode testCaseData) {
        List<CommandDataNoCode> ifCommandDataSet = CommandDataHelper.getCommandDataByName(testCaseData,CommandName.IF);
        List<CommandDataNoCode> endIfCommandDataSet = CommandDataHelper.getCommandDataByName(testCaseData,CommandName.END_IF);
        return ifCommandDataSet.size() == endIfCommandDataSet.size();
    }

    private void assertCommand(Map<String,Boolean> ifCommandByTestCaseMap){
        if(MapUtils.isNotEmpty(ifCommandByTestCaseMap) && ifCommandByTestCaseMap.values().contains(false)){
            ifCommandByTestCaseMap.forEach((testCaseName,v)-> loggerService.warn("You should have the same number of 'if' command and 'end if' command for this test case :"+ testCaseName ));
            throw new IllegalArgumentException("You should have the same number of 'if' command and 'end if' command ");
        }
    }
}
