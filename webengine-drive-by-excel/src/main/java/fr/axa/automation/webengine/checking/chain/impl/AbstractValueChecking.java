package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.constante.PredefinedDateTagValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractValueChecking extends AbstractChecking{
    protected List<String> getPredefinedDataTestValue(List<String> dataTestByColumn){
        return dataTestByColumn.stream().filter(value -> Arrays.asList(PredefinedDateTagValue.values()).contains(value)).collect(Collectors.toList());
    }
}
