package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

public class IfChecking extends AbstractChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList = ((TestSuiteData)testSuiteData).getTestCaseList();
        for(TestCaseData testCaseData : testCaseDataList){
            Set<CommandData> commandDataSet = testCaseData.getCommandList();
            Set<CommandData> ifCommandDataSet = getFilterCommandData(commandDataSet,CommandName.IF);
            Set<CommandData> endIfCommandDataSet = getFilterCommandData(commandDataSet,CommandName.END_IF);
            Assert.isTrue(ifCommandDataSet.size() == endIfCommandDataSet.size(),"You should have the same number of 'if' command and  'end if' command for this test case :"+testCaseData.getName());
        }
        return checkNext(testSuiteData);
    }
}
