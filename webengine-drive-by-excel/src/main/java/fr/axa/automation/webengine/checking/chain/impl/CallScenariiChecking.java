package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CallScenariiChecking extends AbstractChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        Map<String,Set<CommandData>> callCommandMap = null;
        List<TestCaseData> testCaseDataList = ((TestSuiteData)testSuiteData).getTestCaseList();
        List<String> testCaseNameList = getTestCaseNameList(testCaseDataList);

        for(TestCaseData testCaseData : testCaseDataList){
            callCommandMap.put(testCaseData.getName(), getCommandDataByName(testCaseData,CommandName.CALL));
        }

        Map<String,Set<CommandData>> callCommandWhichDoesntExist = getCallCommandWhichDoesntExist(callCommandMap, testCaseNameList);
        assertCommand(callCommandWhichDoesntExist);
        return checkNext(testSuiteData);
    }

    private Map<String,Set<CommandData>> getCallCommandWhichDoesntExist(Map<String,Set<CommandData>> callCommandDataMap, List<String> testCaseNameList) {
        Map<String,Set<CommandData>> callCommandWhichDoesntExist = new HashMap<>();
        for (Map.Entry<String,Set<CommandData>> entry : callCommandDataMap.entrySet()) {
            String testCaseName = entry.getKey();
            Set<CommandData> callCommandSet = entry.getValue();
            callCommandWhichDoesntExist.put(testCaseName, getCallCommandWhichDoesntExist(callCommandSet,testCaseNameList));
        }
        return callCommandWhichDoesntExist;
    }

    private Set<CommandData> getCallCommandWhichDoesntExist(Set<CommandData> callCommandSet, List<String> testCaseNameList) {
        Set<CommandData> callCommandList = new HashSet<>(callCommandSet);
        return callCommandList.stream().filter(commandData -> !testCaseNameList.contains(commandData.getTargetList().get(0))).collect(Collectors.toSet());
    }

    private void assertCommand(Map<String,Set<CommandData>> callCommandWhichDoesntExist){
        boolean throwException = false;
        if(MapUtils.isNotEmpty(callCommandWhichDoesntExist)){
            for (Map.Entry<String,Set<CommandData>> entry : callCommandWhichDoesntExist.entrySet()) {
                String testCaseName = entry.getKey();
                Set<CommandData> callCommandSet = entry.getValue();
                if(CollectionUtils.isNotEmpty(callCommandSet)){
                    loggerService.warn("In this test case "+testCaseName+" Call commands contains values which doesn't exist" + callCommandSet);
                    throwException = true;
                }
            }
            if(throwException){
                throw new IllegalArgumentException("In come cases, 'Call' command values doesn't exist");
            }
        }
    }
}
