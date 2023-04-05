package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.util.ListUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestCaseEndingChecking extends AbstractChecking{

    @Override
    public boolean check(TestSuiteDataDriveByExcel testSuiteData) {
        List<TestCaseDataDriveByExcel> testCaseDataList = ((TestSuiteDataDriveByExcel)testSuiteData).getTestCaseList();
        Map<String,Boolean> testCaseEndingWithRightCommandMap = new HashMap<>();
        for(TestCaseDataDriveByExcel testCaseData : testCaseDataList){
            testCaseEndingWithRightCommandMap.put(testCaseData.getName(),isTestCaseEndingWithRightCommand(testCaseData));
        }
        assertEndScenario(testCaseEndingWithRightCommandMap);
        return checkNext(testSuiteData);
    }

    private boolean isTestCaseEndingWithRightCommand(TestCaseDataDriveByExcel testCaseData) {
        Optional<CommandDataDriveByExcel> commandDataOptional = ListUtil.getLastElement(testCaseData.getCommandList());
        if(commandDataOptional.isPresent()){
            CommandDataDriveByExcel lastCommandData = commandDataOptional.get();
            return lastCommandData.getCommand() == CommandName.END_SCENARII;
        }
        return true;
    }

    private void assertEndScenario(Map<String,Boolean> testCaseEndingWithRightCommandMap){
        if(MapUtils.isNotEmpty(testCaseEndingWithRightCommandMap) && testCaseEndingWithRightCommandMap.values().contains(false)){
            testCaseEndingWithRightCommandMap.forEach((testCaseName,v)-> loggerService.warn("Test case "+testCaseName+" should end with 'End scenarii command'"));
            throw new IllegalArgumentException("Test case should end with 'end scenarii' command ");
        }
    }
}
