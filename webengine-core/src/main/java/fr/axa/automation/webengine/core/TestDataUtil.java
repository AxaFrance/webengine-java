package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.Variable;

import java.util.List;
import java.util.Optional;

public class TestDataUtil {

    public static Optional<TestData> getDataOfTestCase(List<TestData> testDataList, String testCaseName) {
        return testDataList.stream()
                                .filter(elt -> elt.getTestName().equals(testCaseName))
                                .findFirst();
    }

    public static Optional<Variable> getVariableOfTestCase(TestData testData, String variableName) {
        return testData.getData().getVariable().stream()
                                    .filter(elt-> variableName.equalsIgnoreCase(elt.getName()))
                                    .findFirst();
    }

    public static Variable getVariableOfTestCase(List<TestData> testDataList, String testCaseName, String variableName) {
        Variable variable = null;
        Optional<TestData> testData = getDataOfTestCase(testDataList,testCaseName);
        if(testData.isPresent()){
            Optional<Variable> variableByName = getVariableOfTestCase(testData.get(),variableName);
            if(variableByName.isPresent()){
                variable = variableByName.get();
            }
        }
        return variable;
    }

}
