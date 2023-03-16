package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import fr.axa.automation.webengine.util.ListUtil;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestCaseEndingChecking extends AbstractChecking{

    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList = ((TestSuiteData)testSuiteData).getTestCaseList();
        Map<String,Boolean> testCaseEndingWithRightCommandMap = new HashMap<>();
        for(TestCaseData testCaseData : testCaseDataList){
            testCaseEndingWithRightCommandMap.put(testCaseData.getName(),isTestCaseEndingWithRightCommand(testCaseData));
        }
        assertEndScenario(testCaseEndingWithRightCommandMap);
        return checkNext(testSuiteData);
    }

    private boolean isTestCaseEndingWithRightCommand(TestCaseData testCaseData) {
        Optional<CommandData> commandDataOptional = (Optional<CommandData>)ListUtil.getLastElement(testCaseData.getCommandList());
        if(commandDataOptional.isPresent()){
            CommandData lastCommandData = commandDataOptional.get();
            return lastCommandData.getCommand().equalsIgnoreCase(CommandName.END_SCENARII.getName());
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
