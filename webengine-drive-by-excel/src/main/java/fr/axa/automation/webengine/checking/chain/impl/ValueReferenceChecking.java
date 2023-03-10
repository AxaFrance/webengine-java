package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;

import java.util.List;
import java.util.Map;

public class ValueReferenceChecking extends AbstractValueChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList =((TestSuiteData)testSuiteData).getTestCaseList();
        for (TestCaseData testCaseData : testCaseDataList) {
            Map<String,List<String>> allValueReference = getAllValueReference(testCaseData);
            List<String> allId = getAllId(testCaseData);
        }
        return checkNext(testSuiteData);
    }
    



}
