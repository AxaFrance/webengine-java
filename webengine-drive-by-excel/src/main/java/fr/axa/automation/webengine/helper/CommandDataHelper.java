package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.ConstantNoCode;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDataHelper {

    public static List<String> mergeLists(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList<>();

        if (CollectionUtils.isEmpty(list1)) {
            result.addAll(list2);
        } else {
            boolean containsNegation = list1.stream().anyMatch(value-> value.contains("!"));
            if(!containsNegation){
                result.addAll(list1);
            }else{
                if(list1.size()==1){
                    for (String item : list2) {
                        if (!list1.contains(ConstantNoCode.EXCLAMATION_MARK.getValue() + item)) {
                            result.add(item);
                        }
                    }
                }else{
                    result.addAll(list1.stream().filter(value-> !value.contains(ConstantNoCode.EXCLAMATION_MARK.getValue())).collect(Collectors.toList()));
                }
            }
        }
        return result;
    }

    public static boolean canExecuteDataTestColumn(List<String> dataTestRefList,String dataTestColumnName){
        if(CollectionUtils.isNotEmpty(dataTestRefList) && dataTestRefList.size()==1 && StringUtil.equalsIgnoreCase(ConstantNoCode.EXCLAMATION_MARK.getValue(),dataTestRefList.get(0))){
            return false;
        }
        List<String> result = mergeLists(dataTestRefList, Arrays.asList(dataTestColumnName));
        if(result.contains(dataTestColumnName)){
            return true;
        }
        return  false;
    }

    public static List<CommandDataNoCode> getCommandDataByName(TestCaseDataNoCode testCaseData, CommandName commandName) {
        List<CommandDataNoCode> commandDataSet = testCaseData.getCommandList();
        return commandDataSet.stream().filter(commandData -> commandData.getCommand() == commandName).collect(Collectors.toList());
    }
}
