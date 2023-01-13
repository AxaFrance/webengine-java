package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.util.VariableHelperForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class TestCaseDataHelperTest {

    @Test
    void testGetValue() {
        Optional<Variable> variable = TestCaseDataHelper.getValue(VariableHelperForTest.KEY_CHROME,VariableHelperForTest.getVariableList());
        Assertions.assertEquals(VariableHelperForTest.KEY_CHROME,variable.get().getName());
        Assertions.assertEquals(VariableHelperForTest.VALUE_CHROME,variable.get().getValue());
    }
}