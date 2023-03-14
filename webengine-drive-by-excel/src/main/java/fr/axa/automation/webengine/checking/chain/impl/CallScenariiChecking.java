package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CallScenariiChecking extends AbstractChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList = ((TestSuiteData)testSuiteData).getTestCaseList();


        List<String> testCaseNameList = testCaseDataList.stream().map(testCaseData -> testCaseData.getName()).collect(Collectors.toList());
        Set<CommandData> callCommandDataSet = null;

        for(TestCaseData testCaseData : testCaseDataList){
            Set<CommandData> commandDataSet = testCaseData.getCommandList();
            callCommandDataSet = getFilterCommandData(commandDataSet,CommandName.CALL);
        }

        if(CollectionUtils.isNotEmpty(callCommandDataSet) && CollectionUtils.isNotEmpty(testCaseNameList)){
            for (CommandData commandData:callCommandDataSet) {
                Assert.isTrue(testCaseNameList.contains(commandData.getTargetList().get(0)),"This call command "+commandData.getTargetList().get(0)+" contains a test case who doesn't exist");
            }
        }

        return checkNext(testSuiteData);
    }
}
