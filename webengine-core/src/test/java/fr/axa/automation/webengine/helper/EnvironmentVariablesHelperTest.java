package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.util.VariableHelperForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class EnvironmentVariablesHelperTest {


    @Test
    void testGetEnvironnementValue() {
        List<Variable> variableList = VariableHelperForTest.getVariableList();
        Optional<Variable> foundVariable = EnvironmentVariablesHelper.getEnvironnementValue(VariableHelperForTest.KEY_CHROME,variableList);
        foundVariable.ifPresent(variable -> Assertions.assertEquals(VariableHelperForTest.KEY_CHROME, variable.getName()));
    }

    @Test
    void testGetEnvironnementValueWithReference() {
        List<Variable> variableList = VariableHelperForTest.getVariableWithReferenceList();
        Optional<Variable> foundVariable = EnvironmentVariablesHelper.getEnvironnementValue(VariableHelperForTest.KEY_CHROME,variableList);
        foundVariable.ifPresent(variable -> Assertions.assertEquals(VariableHelperForTest.KEY_EDGE_CHROMIUM, variable.getName()));
    }
}