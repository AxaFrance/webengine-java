package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.cmd.PredefinedValue;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
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
    protected final static String DATA_TEST_REFERENCE_REGEX = "[\\w-]*";

    protected List<String> getIdByTestCase(TestCaseDataDriveByExcel testCaseData){
        return testCaseData.getCommandList().stream().map(commandData -> commandData.getId()).collect(Collectors.toList());
    }

    protected Set<String> getDataTestColumnName(TestCaseDataDriveByExcel testCaseData){
        List<CommandDataDriveByExcel>  commandDataList = testCaseData.getCommandList();
        Set<String> dataTestColumn = new HashSet<>();
        if(CollectionUtils.isNotEmpty(commandDataList)){
            Optional<CommandDataDriveByExcel> firstCommandData = commandDataList.stream().findFirst();
            if(firstCommandData.isPresent()){
                dataTestColumn = firstCommandData.get().getDataTestList().keySet();
            }
        }
        return dataTestColumn;
    }

    protected Map<String,Set<String>> getReferencedValueByColumName(TestCaseDataDriveByExcel testCaseData){
        Map<String,Set<String>> dataTestByColunmName = new HashMap<>();
        Set<String> dataTestColumnNameList = getDataTestColumnName(testCaseData);
        for (String dataTestColumnName :dataTestColumnNameList) {
            dataTestByColunmName.put(dataTestColumnName,getReferencedValueByColumName(testCaseData,dataTestColumnName));
        }
        return dataTestByColunmName;
    }

    protected Set<String> getReferencedValueByColumName(TestCaseDataDriveByExcel testCaseData, String dataTestNameColumn){
        Set<String> filterDataTestList = new HashSet<>();
        List<CommandDataDriveByExcel>  commandDataList = testCaseData.getCommandList();
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
