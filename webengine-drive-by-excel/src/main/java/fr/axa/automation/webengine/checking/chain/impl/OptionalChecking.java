package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionalChecking extends AbstractChecking{
    @Override
    public boolean check(TestSuiteDataDriveByExcel testSuiteData) {
        List<TestCaseDataDriveByExcel> testCaseDataList = testSuiteData.getTestCaseList();
        Map<String,Boolean> consistencyOfOptionalCommandMap = new HashMap<>();
        for(TestCaseDataDriveByExcel testCaseData : testCaseDataList){
            consistencyOfOptionalCommandMap.put(testCaseData.getName(), checkConsistencyCommand(testCaseData));
        }
        assertCommand(consistencyOfOptionalCommandMap);
        return checkNext(testSuiteData);
    }

    private boolean checkConsistencyCommand(TestCaseDataDriveByExcel testCaseData) {
        List<CommandDataDriveByExcel> optionalCommandList = getOptionalCommand(testCaseData);
        List<CommandDataDriveByExcel> optionalAndDependsOnPreviousCommandList = getOptionalAndDependsOnPreviousCommand(testCaseData);
        return optionalCommandList.size() == 0 && optionalAndDependsOnPreviousCommandList.size() != 0;
    }

    private void assertCommand(Map<String,Boolean> ifCommandByTestCaseMap){
        if(MapUtils.isNotEmpty(ifCommandByTestCaseMap) && ifCommandByTestCaseMap.values().contains(false)){
            ifCommandByTestCaseMap.forEach((testCaseName,v)-> loggerService.warn("If you use 'optional and depends on previsou, you should have at least one 'optional'. Check this test case :"+ testCaseName ));
            throw new IllegalArgumentException("If you use 'optional and depends on previsou, you should have at least one 'optional'.");
        }
    }
}
