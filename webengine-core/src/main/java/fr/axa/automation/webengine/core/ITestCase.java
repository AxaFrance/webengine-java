package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.generated.Variable;

import java.util.ArrayList;
import java.util.List;

public interface ITestCase {
    List<? extends ITestStep> getTestStepList();
    default boolean isIgnoredAllTestStep() {
        return false;
    }
    default List<Variable> getContextValues() {
        return new ArrayList<>();
    }
}
