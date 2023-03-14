package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReferencedValueChecking extends AbstractValueChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList =((TestSuiteData)testSuiteData).getTestCaseList();
        for (TestCaseData testCaseData : testCaseDataList) {
            Map<String, Set<String>> allValueReference = getAllReferencedValue(testCaseData);
            List<String> allId = getAllId(testCaseData);
            checkReferencedValue(testCaseData.getName(),allValueReference,allId);
        }
        return checkNext(testSuiteData);
    }


    private void checkReferencedValue(String testCaseName, Map<String, Set<String>> allValueReference, List<String> allId){
        for (Map.Entry<String, Set<String>> entry : allValueReference.entrySet()) {
            for (String valueReference  : entry.getValue()) {
                Set<String> valueRefOnly = RegexUtil.match("[\\w-]*",valueReference);
                if(CollectionUtils.isNotEmpty(valueRefOnly)){
                    if(allId.contains(valueRefOnly)){
                        Assert.isTrue(allId.contains(valueRefOnly),"In the test case "+testCaseName+", at the column "+entry.getKey()+" the reference "+valueRefOnly+" doesn't reference any id" );
                    }
                }
            }
        }
    }
}
