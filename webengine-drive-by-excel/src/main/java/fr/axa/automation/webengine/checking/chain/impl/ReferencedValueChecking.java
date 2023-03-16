package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import fr.axa.automation.webengine.util.RegexUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReferencedValueChecking extends AbstractValueChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList =((TestSuiteData)testSuiteData).getTestCaseList();
        List<ReferencedValueAndIdByTestCase> referencedValueAndIdByTestCaseList = new ArrayList<>();
        for (TestCaseData testCaseData : testCaseDataList) {
            Map<String,Set<String>> referencedValueByColumnNameMap = getReferencedValueByColumName(testCaseData);
            List<String> idByTestCaseList = getIdByTestCase(testCaseData);
            referencedValueAndIdByTestCaseList.add(ReferencedValueAndIdByTestCase.builder().testCaseName(testCaseData.getName()).referencedValueByColumNameMap(referencedValueByColumnNameMap).idList(idByTestCaseList).build());
        }
        List<ReferencedValueWhichDoesntExist> referencedValueWhichDoesntExistList = getReferencedValueWhichDoesntExist(referencedValueAndIdByTestCaseList);
        assertValue(referencedValueWhichDoesntExistList);
        return checkNext(testSuiteData);
    }

    private List<ReferencedValueWhichDoesntExist> getReferencedValueWhichDoesntExist(List<ReferencedValueAndIdByTestCase> referencedValueAndIdByTestCaseList){
        List<ReferencedValueWhichDoesntExist> referencedValueWhichDoesntExistList = new ArrayList<>();
        for (ReferencedValueAndIdByTestCase referencedValueAndIdByTestCase : referencedValueAndIdByTestCaseList) {
            referencedValueWhichDoesntExistList.addAll(getReferencedValueWhichDoesntExist(referencedValueAndIdByTestCase));
        }
        return referencedValueWhichDoesntExistList;
    }

    private List<ReferencedValueWhichDoesntExist> getReferencedValueWhichDoesntExist(ReferencedValueAndIdByTestCase referencedValueAndIdByTestCase) {
        List<ReferencedValueWhichDoesntExist> referencedValueWhichDoesntExistList = new ArrayList<>();
        for (Map.Entry<String,Set<String>> entry:referencedValueAndIdByTestCase.getReferencedValueByColumNameMap().entrySet()) {
            String columnName = entry.getKey();
            Set<String> referencedValueSet = entry.getValue();
            referencedValueWhichDoesntExistList.addAll(getReferencedValueWhichDoesntExist(referencedValueAndIdByTestCase.getTestCaseName(),columnName,referencedValueAndIdByTestCase.getIdList(),referencedValueSet));
        }
        return referencedValueWhichDoesntExistList;
    }

    private List<ReferencedValueWhichDoesntExist> getReferencedValueWhichDoesntExist(String testCaseName, String columnName, List<String> idList, Set<String> referencedValueSet) {
        List<ReferencedValueWhichDoesntExist> referencedValueWhichDoesntExistList = new ArrayList<>();
        for (String referencedValue  : referencedValueSet) {
            Set<String> referencedValueOnlySet = RegexUtil.match(DATA_TEST_REFERENCE_REGEX,referencedValue); //<<<num_contrat>>>test<<<num_client>>> or <<<num_contrat>>>
            if(CollectionUtils.isNotEmpty(referencedValueOnlySet)){
                List<String> list = referencedValueOnlySet.stream().filter(rv -> !idList.contains(rv)).collect(Collectors.toList());
                referencedValueWhichDoesntExistList.add(ReferencedValueWhichDoesntExist.builder()
                        .testCaseName(testCaseName)
                        .columnName(columnName)
                        .referencedValueList(list)
                        .build());
            }
        }
        return referencedValueWhichDoesntExistList;
    }

    private void assertValue(List<ReferencedValueWhichDoesntExist> referencedValueWhichDoesntExistList){
        if(CollectionUtils.isNotEmpty(referencedValueWhichDoesntExistList)){
            referencedValueWhichDoesntExistList.forEach(r-> loggerService.warn("This referenced value doesn't exist : "+r));
            throw new IllegalArgumentException("You should have the same number of 'if' command and 'end if' command ");
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @Data
    private class ReferencedValueAndIdByTestCase{
         String testCaseName;
         Map<String,Set<String>> referencedValueByColumNameMap;
         List<String> idList;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder
    @Data
    private class ReferencedValueWhichDoesntExist{
        String testCaseName;
        String columnName;
        List<String> referencedValueList;
    }
}
