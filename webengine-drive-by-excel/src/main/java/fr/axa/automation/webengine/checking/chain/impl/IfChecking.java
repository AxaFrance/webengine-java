package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IfChecking extends AbstractChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList = ((TestSuiteData)testSuiteData).getTestCaseList();
        Map<String,Boolean> consistencyOfIfAndEndIfCommandMap = new HashMap<>();
        for(TestCaseData testCaseData : testCaseDataList){
            consistencyOfIfAndEndIfCommandMap.put(testCaseData.getName(), checkConsistencyCommand(testCaseData));
        }
        assertCommand(consistencyOfIfAndEndIfCommandMap);
        return checkNext(testSuiteData);
    }

    private boolean checkConsistencyCommand(TestCaseData testCaseData) {
        Set<CommandData> ifCommandDataSet = getCommandDataByName(testCaseData,CommandName.IF);
        Set<CommandData> endIfCommandDataSet = getCommandDataByName(testCaseData,CommandName.END_IF);
        return ifCommandDataSet.size() == endIfCommandDataSet.size();
    }

    private void assertCommand(Map<String,Boolean> ifCommandByTestCaseMap){
        if(MapUtils.isNotEmpty(ifCommandByTestCaseMap) && ifCommandByTestCaseMap.values().contains(false)){
            ifCommandByTestCaseMap.forEach((testCaseName,v)-> loggerService.warn("You should have the same number of 'if' command and 'end if' command for this test case :"+ testCaseName ));
            throw new IllegalArgumentException("You should have the same number of 'if' command and 'end if' command ");
        }
    }
}
