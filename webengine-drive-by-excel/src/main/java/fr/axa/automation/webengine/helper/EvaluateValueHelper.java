package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.constante.PredefinedDateTagValue;
import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FormatDate;
import fr.axa.automation.webengine.util.RegexUtil;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EvaluateValueHelper {

    public static String getValueBetweenRafter(String value){
        return StringUtils.substringBetween(value,Constante.TRIPLE_CHEVRON_PREFIX.getValue(),Constante.TRIPLE_CHEVRON_SUFFIX.getValue());
    }

    public static String evaluateValue(String completeValue, List<CommandResult> commandResultList){
        List<String> regexValueList = RegexUtil.match(RegexContante.REFERENCED_VALUE_REGEX, completeValue);
        if(CollectionUtils.isEmpty(regexValueList)){
            return completeValue;
        } else{
            return evaluateValue(completeValue, regexValueList, commandResultList );
        }
    }

    private static String evaluateValue(String completeValue, List<String> regexValueList, List<CommandResult> commandResultList ) {
        String resultValue = completeValue;
        if(CollectionUtils.isNotEmpty(regexValueList)){
            for (String regexValue: regexValueList) {
                String valueWithouRafter = getValueBetweenRafter(regexValue);
                if(PredefinedDateTagValue.isContainsPredefinedDateTagValue(valueWithouRafter)){
                    resultValue = resultValue.replace(regexValue,replaceTagDateValue(valueWithouRafter));
                } else if (PredefinedTagValue.isContainsPredefinedTagValue(valueWithouRafter)) {
                    resultValue = resultValue.replace(regexValue,PredefinedTagValue.valueOf(regexValue).getTagValue());
                } else if (isContainsReferencedValue(valueWithouRafter, commandResultList)) {
                    String referencedValue = getReferencedSaveData(valueWithouRafter,commandResultList);
                    resultValue = resultValue.replace(regexValue,referencedValue);
                }
            }
        }
        return resultValue;
    }

    public static boolean isContainsReferencedValue(String value, List<CommandResult> commandResultList){
        if(CollectionUtils.isNotEmpty(commandResultList)){
            return commandResultList.stream().anyMatch(commandResult -> StringUtil.equalsIgnoreCase(commandResult.getCommandData().getName(),value));
        }
        return false;
    }

    public static String getReferencedSaveData(String value, List<CommandResult> commandResultList){
        if(CollectionUtils.isNotEmpty(commandResultList)){
            List<String> saveDataList = commandResultList.stream()
                                                        .filter(commandResult -> StringUtil.equalsIgnoreCase(commandResult.getCommandData().getName(),value))
                                                        .map(commandResult -> commandResult.getSavedData())
                                                        .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(commandResultList)){
                return saveDataList.get(0);
            }
        }
        return StringUtils.EMPTY;
    }

    private static String replaceTagDateValue(String value){
        String onlyTagValue = getOnlyTagValue(value);
        if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_TODAY.getTagValue()) && value.contains(Constante.MINUS.getValue())) {
            return DateUtil.minusDay(FormatDate.DDMMYYYY,RegexUtil.getNumber(RegexContante.NUMBER_REGEX,value));
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_TODAY.getTagValue()) && value.contains(Constante.PLUS.getValue())) {
            return DateUtil.addDay(FormatDate.DDMMYYYY,RegexUtil.getNumber(RegexContante.NUMBER_REGEX,value));
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_TODAY.getTagValue())) {
            return DateUtil.getDateTime(FormatDate.DDMMYYYY);
        }else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_TODAY_HOUR.getTagValue())) {
            return DateUtil.getDateTime(FormatDate.DDMMYYYYHHMM);
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_ANTERIOR_DAY.getTagValue()) && StringUtil.contains(value, PredefinedDateTagValue.TAG_YESTERDAY.getTagValue())) {
            return DateUtil.minusDay(FormatDate.DDMMYYYY,1);
        }else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_NEXT_DAY.getTagValue()) && StringUtil.contains(value, PredefinedDateTagValue.TAG_PAST_DAY.getTagValue())) {
            return DateUtil.addDay(FormatDate.DDMMYYYY,1);
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_NEXT_MONTH.getTagValue())) {
            return DateUtil.addMonth(FormatDate.DDMMYYYY,1);
        }else {
            return value;
        }
    }

    private static String getOnlyTagValue(String value) {
        if(StringUtils.isNotEmpty(value)){
            return Arrays.asList(value.split("[+-]")).get(0);
        }
        return "";
    }


}
