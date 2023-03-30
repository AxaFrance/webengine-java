package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IfChecking extends AbstractChecking{
    @Override
    public boolean check(TestSuiteDataDriveByExcel testSuiteData) {
        List<TestCaseDataDriveByExcel> testCaseDataList = testSuiteData.getTestCaseList();
        Map<String,Boolean> consistencyOfIfAndEndIfCommandMap = new HashMap<>();
        for(TestCaseDataDriveByExcel testCaseData : testCaseDataList){
            consistencyOfIfAndEndIfCommandMap.put(testCaseData.getName(), checkConsistencyCommand(testCaseData));
        }
        assertCommand(consistencyOfIfAndEndIfCommandMap);
        return checkNext(testSuiteData);
    }

    private boolean checkConsistencyCommand(TestCaseDataDriveByExcel testCaseData) {
        List<CommandDataDriveByExcel> ifCommandDataSet = getCommandDataByName(testCaseData,CommandName.IF);
        List<CommandDataDriveByExcel> endIfCommandDataSet = getCommandDataByName(testCaseData,CommandName.END_IF);
        return ifCommandDataSet.size() == endIfCommandDataSet.size();
    }

    private void assertCommand(Map<String,Boolean> ifCommandByTestCaseMap){
        if(MapUtils.isNotEmpty(ifCommandByTestCaseMap) && ifCommandByTestCaseMap.values().contains(false)){
            ifCommandByTestCaseMap.forEach((testCaseName,v)-> loggerService.warn("You should have the same number of 'if' command and 'end if' command for this test case :"+ testCaseName ));
            throw new IllegalArgumentException("You should have the same number of 'if' command and 'end if' command ");
        }
    }
}
