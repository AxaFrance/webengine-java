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
        if(foundVariable.isPresent()){
            Assertions.assertEquals(VariableHelperForTest.KEY_CHROME,foundVariable.get().getName());
        }
    }

    @Test
    void testGetEnvironnementValueWithReference() {
        List<Variable> variableList = VariableHelperForTest.getVariableWithReferenceList();
        Optional<Variable> foundVariable = EnvironmentVariablesHelper.getEnvironnementValue(VariableHelperForTest.KEY_CHROME,variableList);
        if(foundVariable.isPresent()){
            Assertions.assertEquals(VariableHelperForTest.KEY_EDGE_CHROMIUM,foundVariable.get().getName());
        }
    }
}