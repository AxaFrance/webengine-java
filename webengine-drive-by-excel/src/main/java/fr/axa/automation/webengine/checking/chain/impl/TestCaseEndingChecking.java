package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import fr.axa.automation.webengine.util.ListUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestCaseEndingChecking extends AbstractChecking{

    @Override
    public boolean check(TestSuiteDataNoCode testSuiteData) {
        List<TestCaseDataNoCode> testCaseDataList = ((TestSuiteDataNoCode)testSuiteData).getTestCaseList();
        Map<String,Boolean> testCaseEndingWithRightCommandMap = new HashMap<>();
        for(TestCaseDataNoCode testCaseData : testCaseDataList){
            testCaseEndingWithRightCommandMap.put(testCaseData.getName(),isTestCaseEndingWithRightCommand(testCaseData));
        }
        assertEndScenario(testCaseEndingWithRightCommandMap);
        return checkNext(testSuiteData);
    }

    private boolean isTestCaseEndingWithRightCommand(TestCaseDataNoCode testCaseData) {
        Optional<CommandDataNoCode> commandDataOptional = ListUtil.getLastElement(testCaseData.getCommandList());
        if(commandDataOptional.isPresent()){
            CommandDataNoCode lastCommandData = commandDataOptional.get();
            return lastCommandData.getCommand() == CommandName.END_SCENARIO;
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
