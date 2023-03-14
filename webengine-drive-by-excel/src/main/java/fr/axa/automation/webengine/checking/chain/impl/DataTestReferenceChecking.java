package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.object.TestSuiteData;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataTestReferenceChecking extends AbstractValueChecking{

    private final static List<String> KEYWORD_DATA_REFERENCE = Arrays.asList("!");

    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        List<TestCaseData> testCaseDataList =((TestSuiteData)testSuiteData).getTestCaseList();
        List<String> dataTestReferenceList;
        Set<String> dataTestNameColumnList;
        for (TestCaseData testCaseData : testCaseDataList) {
            dataTestNameColumnList = getDataTestNameColumn(testCaseData);
            dataTestReferenceList = getAllDataReference(testCaseData);
            checkDataTestReference(testCaseData.getName(), dataTestNameColumnList, dataTestReferenceList);
        }
        return checkNext(testSuiteData);
    }

    protected List<String> getAllDataReference(TestCaseData testCaseData){
        return testCaseData.getCommandList().stream().filter(commandData -> !KEYWORD_DATA_REFERENCE.contains(StringUtils.trim(commandData.getDataTestReference())))
                .map(commandData -> commandData.getDataTestReference())
                .collect(Collectors.toList());
    }

    protected void checkDataTestReference(String testCaseName, Set<String> dataTestNameColumnList, List<String> dataTestReferenceList){
        for (String dataTestReference : dataTestReferenceList) {
            List<String> dataTestRefOfOneCommandList = Arrays.asList(dataTestReference.split(Constante.SEMICOLON.getValue()));

        }
    }
}
