package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.helper.TestCaseHelperDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataTestReferenceChecking extends AbstractValueChecking{

    private final static List<String> KEYWORD_DATA_REFERENCE = Arrays.asList("!");

    @Override
    public boolean check(TestSuiteDataDriveByExcel testSuiteData) {
        List<TestCaseDataDriveByExcel> testCaseDataList =((TestSuiteDataDriveByExcel)testSuiteData).getTestCaseList();
        Map<String,List<String>> dataTestReferenceWhichDoesntExistMap = new HashMap<>();
        for (TestCaseDataDriveByExcel testCaseData : testCaseDataList) {
            List<String> dataTestReferenceList = getDataTestReference(testCaseData);
            List<String> dataTestColumnNameList = TestCaseHelperDriveByExcel.getDataTestColumnName(testCaseData);
            if(CollectionUtils.isNotEmpty(dataTestReferenceList)){
                dataTestReferenceWhichDoesntExistMap.put(testCaseData.getName(),getDataTestReferenceWhichDoesntExist(dataTestReferenceList, dataTestColumnNameList));
            }
        }
        if(MapUtils.isNotEmpty(dataTestReferenceWhichDoesntExistMap)){
            assertDataTestReference(dataTestReferenceWhichDoesntExistMap);
        }
        return checkNext(testSuiteData);
    }

    protected List<String> getDataTestReference(TestCaseDataDriveByExcel testCaseData){
        return testCaseData.getCommandList()
                .stream()
                .filter(commandData -> StringUtils.isNotEmpty(StringUtils.trim(commandData.getDataTestReference())) && !KEYWORD_DATA_REFERENCE.contains(StringUtils.trim(commandData.getDataTestReference())))
                .map(commandData -> commandData.getDataTestReference())
                .collect(Collectors.toList());
    }

    protected List<String> getDataTestReferenceWhichDoesntExist(List<String> dataTestReferenceList, List<String> dataTestColumnNameList ){
        List<String> dataTestReferenceWhichDoesntExistList = new ArrayList<>();
        for (String dataTestReference : dataTestReferenceList) {
            List<String> dataTestReferenceInOneCommandList = Arrays.asList(dataTestReference.split(Constante.SEMICOLON.getValue())); //data-test-auto-rec;!data-test-auto-rec
            dataTestReferenceWhichDoesntExistList.addAll(getDataTestReferenceWhichDoesntInOneCmdExist(dataTestReferenceInOneCommandList,dataTestColumnNameList));
        }
        return dataTestReferenceWhichDoesntExistList;
    }

    private List<String> getDataTestReferenceWhichDoesntInOneCmdExist(List<String> dataTestReferenceInOneCommandList, List<String> dataTestColumnNameList) {
        List<String> dataTestReferenceWhichDoesntExistList = new ArrayList<>();
        for (String dataTestReference : dataTestReferenceInOneCommandList) {
            Optional<String> dataTestReferenceOptional = RegexUtil.findFirst(RegexContante.DATA_TEST_REFERENCE_REGEX,dataTestReference);// !data-test-auto-rec
            if (dataTestReferenceOptional.isPresent() && !dataTestColumnNameList.contains(dataTestReferenceOptional.get())){
                dataTestReferenceWhichDoesntExistList.add(dataTestReferenceOptional.get());
            }
        }
        return dataTestReferenceWhichDoesntExistList;
    }

    private void assertDataTestReference(Map<String,List<String>> dataTestReferenceWhichDoesntExistList){
        if(MapUtils.isNotEmpty(dataTestReferenceWhichDoesntExistList)){
            dataTestReferenceWhichDoesntExistList.forEach((testCaseName,v)-> loggerService.warn("In this test case "+ testCaseName +" these data test reference doesn't exist"+v ));
            throw new IllegalArgumentException("Some test case have data test reference which doesn't exist ");
        }
    }
}
