package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IfChecking extends AbstractChecking{
    @Override
    public boolean check(TestSuiteDataDriveByExcel testSuiteData) {
        List<TestCaseDataDriveByExcel> testCaseDataList = ((TestSuiteDataDriveByExcel)testSuiteData).getTestCaseList();
        Map<String,Boolean> consistencyOfIfAndEndIfCommandMap = new HashMap<>();
        for(TestCaseDataDriveByExcel testCaseData : testCaseDataList){
            consistencyOfIfAndEndIfCommandMap.put(testCaseData.getName(), checkConsistencyCommand(testCaseData));
        }
        assertCommand(consistencyOfIfAndEndIfCommandMap);
        return checkNext(testSuiteData);
    }

    private boolean checkConsistencyCommand(TestCaseDataDriveByExcel testCaseData) {
        Set<CommandDataDriveByExcel> ifCommandDataSet = getCommandDataByName(testCaseData,CommandName.IF);
        Set<CommandDataDriveByExcel> endIfCommandDataSet = getCommandDataByName(testCaseData,CommandName.END_IF);
        return ifCommandDataSet.size() == endIfCommandDataSet.size();
    }

    private void assertCommand(Map<String,Boolean> ifCommandByTestCaseMap){
        if(MapUtils.isNotEmpty(ifCommandByTestCaseMap) && ifCommandByTestCaseMap.values().contains(false)){
            ifCommandByTestCaseMap.forEach((testCaseName,v)-> loggerService.warn("You should have the same number of 'if' command and 'end if' command for this test case :"+ testCaseName ));
            throw new IllegalArgumentException("You should have the same number of 'if' command and 'end if' command ");
        }
    }
}
