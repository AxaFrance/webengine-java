package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import fr.axa.automation.webengine.util.ListUtil;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

public class TestCaseEndingChecking extends AbstractChecking{

    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList = ((TestSuiteData)testSuiteData).getTestCaseList();
        for(TestCaseData testCaseData : testCaseDataList){
            Optional<CommandData> commandDataOptional = (Optional<CommandData>)ListUtil.getLastElement(testCaseData.getCommandList());
            if(commandDataOptional.isPresent()){
                CommandData lastCommandData = commandDataOptional.get();
                Assert.isTrue(lastCommandData.getCommand().equalsIgnoreCase(CommandName.END_SCENARII.getName()),"The last command for this test case :"+testCaseData.getName()+" isn't "+CommandName.END_SCENARII.getName());
            }
        }
        return checkNext(testSuiteData);
    }
}
