package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.Variable;

import java.util.List;
import java.util.Optional;

public final class TestDataHelper {

    private TestDataHelper() {
    }

    public static Optional<TestData> getDataOfTestCase(List<TestData> testDataList, String testCaseName) {
        return testDataList.stream()
                            .filter(testData -> testData.getTestName().equals(testCaseName))
                            .findFirst();
    }
    public static Variable getVariableOfTestCase(List<TestData> testDataList, String testCaseName, String variableName) {
        Optional<TestData> testData = getDataOfTestCase(testDataList,testCaseName);
        if(testData.isPresent()){
            Optional<Variable> variableByName = getVariableOfTestCase(testData.get(),variableName);
            if(variableByName.isPresent()){
                return variableByName.get();
            }
        }
        return null;
    }

    public static Optional<Variable> getVariableOfTestCase(TestData testData, String variableName) {
        return testData.getData().getVariables().stream()
                                    .filter(elt-> variableName.equalsIgnoreCase(elt.getName()))
                                    .findFirst();
    }
}
