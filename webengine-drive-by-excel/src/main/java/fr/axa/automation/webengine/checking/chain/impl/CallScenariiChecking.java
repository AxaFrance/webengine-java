package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.TargetKey;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
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
    public boolean check(TestSuiteDataDriveByExcel testSuiteData) {
        Map<String,List<CommandDataDriveByExcel>> callCommandByTestCaseMap = new HashMap<>();
        List<TestCaseDataDriveByExcel> testCaseDataList = testSuiteData.getTestCaseList();
        List<String> testCaseNameList = getTestCaseNameList(testCaseDataList);

        for(TestCaseDataDriveByExcel testCaseData : testCaseDataList){
            List<CommandDataDriveByExcel> commandCallList = getCommandDataByName(testCaseData,CommandName.CALL);
            if(CollectionUtils.isNotEmpty(commandCallList)){
                callCommandByTestCaseMap.put(testCaseData.getName(), getCommandDataByName(testCaseData,CommandName.CALL));
            }
        }
        if(MapUtils.isNotEmpty(callCommandByTestCaseMap)){
            Map<String,Set<CommandDataDriveByExcel>> callCommandWhichDoesntExist = getCallCommandWhichDoesntExist(callCommandByTestCaseMap, testCaseNameList);
            assertCommand(callCommandWhichDoesntExist);
        }

        return checkNext(testSuiteData);
    }

    private Map<String,Set<CommandDataDriveByExcel>> getCallCommandWhichDoesntExist(Map<String,List<CommandDataDriveByExcel>> callCommandDataMap, List<String> testCaseNameList) {
        Map<String,Set<CommandDataDriveByExcel>> callCommandWhichDoesntExist = new HashMap<>();
        for (Map.Entry<String,List<CommandDataDriveByExcel>> entry : callCommandDataMap.entrySet()) {
            String testCaseName = entry.getKey();
            List<CommandDataDriveByExcel> callCommandSet = entry.getValue();
            List<String> filterTestCaseNameList = testCaseNameList.stream().filter(tcName -> !tcName.equalsIgnoreCase(testCaseName)).collect(Collectors.toList());
            callCommandWhichDoesntExist.put(testCaseName, getCallCommandWhichDoesntExist(callCommandSet,filterTestCaseNameList));
        }
        return callCommandWhichDoesntExist;
    }

    private Set<CommandDataDriveByExcel> getCallCommandWhichDoesntExist(List<CommandDataDriveByExcel> callCommandSet, List<String> testCaseNameList) {
        Set<CommandDataDriveByExcel> callCommandList = new HashSet<>(callCommandSet);
        return callCommandList.stream().filter(commandData -> !testCaseNameList.contains(commandData.getTargetList().get(TargetKey.CALL))).collect(Collectors.toSet());
    }

    private void assertCommand(Map<String,Set<CommandDataDriveByExcel>> callCommandWhichDoesntExist){
        boolean throwException = false;
        if(MapUtils.isNotEmpty(callCommandWhichDoesntExist)){
            for (Map.Entry<String,Set<CommandDataDriveByExcel>> entry : callCommandWhichDoesntExist.entrySet()) {
                String testCaseName = entry.getKey();
                Set<CommandDataDriveByExcel> callCommandSet = entry.getValue();
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
