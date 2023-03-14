package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.PredefinedValue;
import fr.axa.automation.webengine.object.CommandData;
import fr.axa.automation.webengine.object.TestCaseData;
import fr.axa.automation.webengine.util.RegexUtil;
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

    protected final static String VALUE_REFERENCE_REGEX = "(<<<.*?>>>)?";

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

    protected Map<String,Set<String>> getAllReferencedValue(TestCaseData testCaseData){
        Set<CommandData>  commandDataList = testCaseData.getCommandList();
        Set<String> dataTestNameColumnList = getDataTestNameColumn(testCaseData);
        Map<String,Set<String>> allDataTestByColumnName = new HashMap<>();
        dataTestNameColumnList.stream().forEach(dataTestNameColumn -> {
            Set<String> dataTestByColunm = getReferencedValueByDataTestColumn(testCaseData,dataTestNameColumn);
            allDataTestByColumnName.put(dataTestNameColumn,dataTestByColunm);
        });
        return allDataTestByColumnName;
    }

    protected Set<String> getReferencedValueByDataTestColumn(TestCaseData testCaseData, String dataTestNameColumn){
        Set<String> filterDataTestList = new HashSet<>();
        Set<CommandData>  commandDataList = testCaseData.getCommandList();
        List<String> dataTestByColumn = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            dataTestByColumn = commandDataList.stream().map(commandData -> commandData.getDataTestList().get(dataTestNameColumn)).collect(Collectors.toList());
        }

        dataTestByColumn.stream().forEach(value -> filterDataTestList.addAll(RegexUtil.match(VALUE_REFERENCE_REGEX,value)));
        return filterDataTestList.stream().filter(value -> !Arrays.asList(PredefinedValue.values()).contains(value))
                                          .collect(Collectors.toSet());

    }

    protected List<String> getPredefinedDataTestValue(List<String> dataTestByColumn){
        return dataTestByColumn.stream().filter(value -> Arrays.asList(PredefinedValue.values()).contains(value)).collect(Collectors.toList());
    }
}
