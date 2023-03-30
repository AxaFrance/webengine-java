package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.PredefinedValue;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractValueChecking extends AbstractChecking{





    protected List<String> getPredefinedDataTestValue(List<String> dataTestByColumn){
        return dataTestByColumn.stream().filter(value -> Arrays.asList(PredefinedValue.values()).contains(value)).collect(Collectors.toList());
    }


}
