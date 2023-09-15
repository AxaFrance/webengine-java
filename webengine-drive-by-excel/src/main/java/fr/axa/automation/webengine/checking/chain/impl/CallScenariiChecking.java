package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.helper.CommandDataHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
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
    public boolean check(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData) {
        Map<String,List<CommandDataNoCode>> callCommandByTestCaseMap = new HashMap<>();
        List<TestCaseDataNoCode> testCaseDataList = testSuiteData.getTestCaseList();
        List<String> testCaseNameList = getTestCaseNameList(testCaseDataList);

        for(TestCaseDataNoCode testCaseData : testCaseDataList){
            List<CommandDataNoCode> commandCallList = CommandDataHelper.getCommandDataByName(testCaseData,CommandName.CALL);
            if(CollectionUtils.isNotEmpty(commandCallList)){
                callCommandByTestCaseMap.put(testCaseData.getName(), CommandDataHelper.getCommandDataByName(testCaseData,CommandName.CALL));
            }
        }
        if(MapUtils.isNotEmpty(callCommandByTestCaseMap)){
            Map<String,Set<CommandDataNoCode>> callCommandWhichDoesntExist = getCallCommandWhichDoesntExist(callCommandByTestCaseMap, testCaseNameList);
            assertCommand(callCommandWhichDoesntExist);
        }

        return checkNext(globalApplicationContext,testSuiteData);
    }

    private Map<String,Set<CommandDataNoCode>> getCallCommandWhichDoesntExist(Map<String,List<CommandDataNoCode>> callCommandDataMap, List<String> testCaseNameList) {
        Map<String,Set<CommandDataNoCode>> callCommandWhichDoesntExist = new HashMap<>();
        for (Map.Entry<String,List<CommandDataNoCode>> entry : callCommandDataMap.entrySet()) {
            String testCaseName = entry.getKey();
            List<CommandDataNoCode> callCommandSet = entry.getValue();
            List<String> filterTestCaseNameList = testCaseNameList.stream().filter(tcName -> !tcName.equalsIgnoreCase(testCaseName)).collect(Collectors.toList());
            callCommandWhichDoesntExist.put(testCaseName, getCallCommandWhichDoesntExist(callCommandSet,filterTestCaseNameList));
        }
        return callCommandWhichDoesntExist;
    }

    private Set<CommandDataNoCode> getCallCommandWhichDoesntExist(List<CommandDataNoCode> callCommandSet, List<String> testCaseNameList) {
        Set<CommandDataNoCode> callCommandList = new HashSet<>(callCommandSet);
        return callCommandList.stream().filter(commandData -> !testCaseNameList.contains(commandData.getTargetList().get(TargetKey.CALL))).collect(Collectors.toSet());
    }

    private void assertCommand(Map<String,Set<CommandDataNoCode>> callCommandWhichDoesntExist){
        boolean throwException = false;
        if(MapUtils.isNotEmpty(callCommandWhichDoesntExist)){
            for (Map.Entry<String,Set<CommandDataNoCode>> entry : callCommandWhichDoesntExist.entrySet()) {
                String testCaseName = entry.getKey();
                Set<CommandDataNoCode> callCommandSet = entry.getValue();
                if(CollectionUtils.isNotEmpty(callCommandSet)){
                    loggerService.warn("In this test case '"+testCaseName+"', Call command contains a target which doesn't exist." + callCommandSet);
                    throwException = true;
                }
            }
            if(throwException){
                throw new IllegalArgumentException("In some test cases, Call command contains a target which doesn't exist. See warning above.");
            }
        }
    }
}
