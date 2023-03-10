package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.PredefinedValue;
import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractValueChecking extends AbstractChecking{

    private final static String VALUE_REFERENCE_REGEX = "^[<]{3}.*[>]{3}$";
    private final static String PREDEFINED_VALUE_REGEX = "[<]{3}.*[>]{3}";

    protected Map<String,List<String>> getAllValueReference(TestCaseData testCaseData){
        Set<CommandData>  commandDataList = testCaseData.getCommandList();
        Set<String> dataTestNameColumnList = getDataTestNameColumn(testCaseData);
        Map<String,List<String>> allDataTestByColumnName = new HashMap<>();
        dataTestNameColumnList.stream().forEach(dataTestNameColumn -> {
            List<String> dataTestByColunm = getDataTestWithReference(testCaseData,dataTestNameColumn);
            allDataTestByColumnName.put(dataTestNameColumn,dataTestByColunm);
        });
        return allDataTestByColumnName;
    }

    protected List<String> getAllId(TestCaseData testCaseData){
        return testCaseData.getCommandList().stream().map(commandData -> commandData.getId()).collect(Collectors.toList());
    }

    protected Set<String> getDataTestNameColumn(TestCaseData testCaseData){
        Set<CommandData>  commandDataList = testCaseData.getCommandList();
        Set<String> dataTestColumn = new HashSet<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            Optional<CommandData> firstCommandData = commandDataList.stream().findFirst();
            if(firstCommandData.isPresent()){
                dataTestColumn = firstCommandData.get().getDataTestList().keySet();
            }
        }
        return dataTestColumn;
    }

    protected List<String> getDataTestWithReference(TestCaseData testCaseData, String dataTestNameColumn){
        Set<CommandData>  commandDataList = testCaseData.getCommandList();
        List<String> dataTestByColumn = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            dataTestByColumn = commandDataList.stream().map(commandData -> commandData.getDataTestList().get(dataTestNameColumn)).collect(Collectors.toList());
        }
        return dataTestByColumn.stream().filter(value -> value.matches(VALUE_REFERENCE_REGEX))
                                        .filter(value -> !Arrays.asList(PredefinedValue.values()).contains(value))
                                        .collect(Collectors.toList());
    }

    protected List<String> getPredefinedValue(List<String> dataTestByColumn){
        return dataTestByColumn.stream().filter(value -> Arrays.asList(PredefinedValue.values()).contains(value)).collect(Collectors.toList());
    }
}
