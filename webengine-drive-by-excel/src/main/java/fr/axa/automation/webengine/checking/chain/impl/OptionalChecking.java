package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

public class OptionalChecking extends AbstractChecking{
    @Override
    public boolean check(TestSuiteDataDriveByExcel testSuiteData) {
        List<TestCaseDataDriveByExcel> testCaseDataList = testSuiteData.getTestCaseList();
        List<NumberOfOptionalByTestCase> numberOfOptionalList = new ArrayList<>();
        for(TestCaseDataDriveByExcel testCaseData : testCaseDataList){
            numberOfOptionalList.add(getNumberOfOptional(testCaseData));
        }
        assertCommand(numberOfOptionalList);
        return checkNext(testSuiteData);
    }

    private NumberOfOptionalByTestCase getNumberOfOptional(TestCaseDataDriveByExcel testCaseData) {
        List<CommandDataDriveByExcel> optionalCommandList = getOptionalCommand(testCaseData);
        List<CommandDataDriveByExcel> optionalAndDependsOnPreviousCommandList = getOptionalAndDependsOnPreviousCommand(testCaseData);
        return NumberOfOptionalByTestCase.builder().testCaseName(testCaseData.getName()).numberOfCommandWithOptional(optionalCommandList.size()).numberOfCommandWithOptionalAndDependsOnPrevious(optionalAndDependsOnPreviousCommandList.size()).build();
    }

    private void assertCommand(List<NumberOfOptionalByTestCase> numberOfOptionalList){
        boolean throwException=false;
        for (NumberOfOptionalByTestCase numberOfOptionalByTestCase :numberOfOptionalList){
            if(numberOfOptionalByTestCase.getNumberOfCommandWithOptionalAndDependsOnPrevious()>0 && numberOfOptionalByTestCase.getNumberOfCommandWithOptional()==0){
                loggerService.warn("If you use 'optional and depends on previsou, you should have at least one 'optional'. Check this test case :"+ numberOfOptionalByTestCase.getTestCaseName());
            }
        }

        if(throwException){
            throw new IllegalArgumentException("If you use 'optional and depends on previsou, you should have at least one 'optional'.");
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    @Builder
    private static class NumberOfOptionalByTestCase{
        String testCaseName;
        Integer numberOfCommandWithOptional;
        Integer numberOfCommandWithOptionalAndDependsOnPrevious;
    }
}
