package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FormatDate;
import fr.axa.automation.webengine.util.RegexUtil;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EvaluateValueHelper {

    public static String getValueBetweenRafter(String value){
        return StringUtils.substringBetween(value,Constante.TRIPLE_CHEVRON_PREFIX.getValue(),Constante.TRIPLE_CHEVRON_SUFFIX.getValue());
    }

    public static String evaluateValue(String completeValue, Map<String, CommandResult> commandResultMap){
        List<String> regexValueList = RegexUtil.match(RegexContante.REFERENCED_VALUE_REGEX, completeValue);
        if(CollectionUtils.isEmpty(regexValueList)){
            return completeValue;
        } else{
            return evaluateValue(completeValue, regexValueList, commandResultMap );
        }
    }

    private static String evaluateValue(String completeValue, List<String> regexValueList, Map<String, CommandResult> commandResultMap ) {
        String resultValue = completeValue;
        if(CollectionUtils.isNotEmpty(regexValueList)){
            for (String regexValue: regexValueList) {
                String valueWithouRafter = getValueBetweenRafter(regexValue);;
                if(isContainsTagDateValue(valueWithouRafter)){
                    resultValue = resultValue.replace(regexValue,replaceTagDateValue(valueWithouRafter));
                }
                if (isContainsReferencedValue(valueWithouRafter, commandResultMap)) {
                    String referencedValue = commandResultMap.get(StringUtil.removeSpecialCharacters(valueWithouRafter)).getSavedData();
                    resultValue = resultValue.replace(regexValue,referencedValue);
                }
            }
        }
        return resultValue;
    }

    public static boolean isContainsTagDateValue(String value){
        List<String> list = PredefinedTagValue.getTagValueList().stream().filter(predifinedTagValue -> StringUtil.contains(value,predifinedTagValue)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(list);
    }

    public static boolean isContainsReferencedValue(String value, Map<String, CommandResult> commandResultMap){
        if(MapUtils.isNotEmpty(commandResultMap)){
            return commandResultMap.keySet().contains(StringUtil.removeSpecialCharacters(value));
        }
        return false;
    }

    private static String replaceTagDateValue(String value){
        String onlyTagValue = getOnlyTagValue(value);
        if (StringUtils.equalsIgnoreCase(onlyTagValue,PredefinedTagValue.TAG_TODAY.getTagValue()) && value.contains(Constante.MINUS.getValue())) {
            return DateUtil.minusDay(FormatDate.DDMMYYYY,getNumber(value));
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue,PredefinedTagValue.TAG_TODAY.getTagValue()) && value.contains(Constante.PLUS.getValue())) {
            return DateUtil.addDay(FormatDate.DDMMYYYY,getNumber(value));
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue,PredefinedTagValue.TAG_TODAY.getTagValue())) {
            return DateUtil.getDateTime(FormatDate.DDMMYYYY);
        }else if (StringUtils.equalsIgnoreCase(onlyTagValue,PredefinedTagValue.TAG_TODAY_HOUR.getTagValue())) {
            return DateUtil.getDateTime(FormatDate.DDMMYYYYHHMM);
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue,PredefinedTagValue.TAG_ANTERIOR_DAY.getTagValue()) && StringUtil.contains(value,PredefinedTagValue.TAG_YESTERDAY.getTagValue())) {
            return DateUtil.minusDay(FormatDate.DDMMYYYY,1);
        }else if (StringUtils.equalsIgnoreCase(onlyTagValue,PredefinedTagValue.TAG_NEXT_DAY.getTagValue()) && StringUtil.contains(value,PredefinedTagValue.TAG_PAST_DAY.getTagValue())) {
            return DateUtil.addDay(FormatDate.DDMMYYYY,1);
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue,PredefinedTagValue.TAG_NEXT_MONTH.getTagValue())) {
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

    private static Integer getNumber(String value){
        Integer number = 0;
        List<String> regexValueList = RegexUtil.match(RegexContante.NUMBER_REGEX, value);
        if (CollectionUtils.isNotEmpty(regexValueList)) {
           return Integer.parseInt(regexValueList.get(0));
        }
        return number;
    }
}
