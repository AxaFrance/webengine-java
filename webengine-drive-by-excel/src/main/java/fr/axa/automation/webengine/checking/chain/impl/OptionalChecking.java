package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

public class OptionalChecking extends AbstractChecking{
    @Override
    public boolean check(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData) {
        List<TestCaseDataNoCode> testCaseDataList = testSuiteData.getTestCaseList();
        List<NumberOfOptionalByTestCase> numberOfOptionalList = new ArrayList<>();
        for(TestCaseDataNoCode testCaseData : testCaseDataList){
            numberOfOptionalList.add(getNumberOfOptional(testCaseData));
        }
        assertCommand(numberOfOptionalList);
        return checkNext(globalApplicationContext,testSuiteData);
    }

    private NumberOfOptionalByTestCase getNumberOfOptional(TestCaseDataNoCode testCaseData) {
        List<CommandDataNoCode> optionalCommandList = getOptionalCommand(testCaseData);
        List<CommandDataNoCode> optionalAndDependsOnPreviousCommandList = getOptionalAndDependsOnPreviousCommand(testCaseData);
        return NumberOfOptionalByTestCase.builder().testCaseName(testCaseData.getName()).numberOfCommandWithOptional(optionalCommandList.size()).numberOfCommandWithOptionalAndDependsOnPrevious(optionalAndDependsOnPreviousCommandList.size()).build();
    }

    private void assertCommand(List<NumberOfOptionalByTestCase> numberOfOptionalList){
        boolean throwException=false;
        for (NumberOfOptionalByTestCase numberOfOptionalByTestCase :numberOfOptionalList){
            if(numberOfOptionalByTestCase.getNumberOfCommandWithOptionalAndDependsOnPrevious()>0 && numberOfOptionalByTestCase.getNumberOfCommandWithOptional()==0){
                loggerService.warn("If you use the option 'optional and depends on previous', you should have at least one 'optional'. Check this test case :"+ numberOfOptionalByTestCase.getTestCaseName());
                throwException=true;
            }
        }

        if(throwException){
            throw new IllegalArgumentException("If you use the option 'optional and depends on previous', you should have at least one 'optional'.");
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
