package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.constante.PredefinedDateTagValue;
import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.global.AbstractSettings;
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

    public static String getValueBetweenBrackets(String value){
        return StringUtils.substringBetween(value,Constante.BRACKETS_PREFIX.getValue(),Constante.BRACKETS_SUFFIX.getValue());
    }

    public static String getValueBetweenRafter(String value,String prefix, String suffix){
        return StringUtils.substringBetween(value,prefix,suffix);
    }

    public static String evaluateValue(AbstractSettings settings, String completeValue, List<CommandResult> commandResultList){
        List<String> integrationRegexValueList = RegexUtil.match(RegexContante.INTEGRATION_REGEX_VALUE, completeValue);
        List<String> referencedRegexValueList = RegexUtil.match(RegexContante.REFERENCED_REGEX_VALUE, completeValue);
        String evaluateValue = completeValue;
        if(CollectionUtils.isNotEmpty(integrationRegexValueList)){
            evaluateValue = evaluateIntegrationRegexValue(completeValue, integrationRegexValueList, settings);
        }
        if(CollectionUtils.isNotEmpty(referencedRegexValueList)){
            evaluateValue = evaluateReferencedRegexValue(evaluateValue, referencedRegexValueList, commandResultList );
        }
        return evaluateValue;
    }

    public static String evaluateIntegrationRegexValue(String completeValue, List<String> integrationRegexValueList,AbstractSettings settings) {
        String resultValue = completeValue;
        if (CollectionUtils.isNotEmpty(integrationRegexValueList)) {
            for (String integrationRegexValue : integrationRegexValueList) {
                String valueWithouBrackets = getValueBetweenBrackets(integrationRegexValue);
                resultValue = resultValue.replace(integrationRegexValue, settings.getValues().get(valueWithouBrackets));
            }
        }
        return resultValue;
    }

    private static String evaluateReferencedRegexValue(String completeValue, List<String> referencedRegexValueList, List<CommandResult> commandResultList ) {
        String resultValue = completeValue;
        if(CollectionUtils.isNotEmpty(referencedRegexValueList)){
            for (String referencedRegexValue: referencedRegexValueList) {
                String valueWithouRafter = getValueBetweenRafter(referencedRegexValue);
                if(PredefinedDateTagValue.isContainsPredefinedDateTagValue(valueWithouRafter)) {
                    resultValue = resultValue.replace(referencedRegexValue,replaceTagDateValue(valueWithouRafter));
                } else if (PredefinedTagValue.isContainsPredefinedTagValue(valueWithouRafter)) {
                    resultValue = resultValue.replace(referencedRegexValue,PredefinedTagValue.valueOf(valueWithouRafter).getTagValue());
                } else if (isContainsReferencedValue(valueWithouRafter, commandResultList)) {
                    String savedData = getSavedData(valueWithouRafter,commandResultList);
                    resultValue = resultValue.replace(referencedRegexValue,savedData);
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

    public static String getSavedData(String value, List<CommandResult> commandResultList){
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
            return DateUtil.minusDay(FormatDate.DDMMYYYY,RegexUtil.getNumber(RegexContante.REGEX_NUMBER,value));
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_TODAY.getTagValue()) && value.contains(Constante.PLUS.getValue())) {
            return DateUtil.addDay(FormatDate.DDMMYYYY,RegexUtil.getNumber(RegexContante.REGEX_NUMBER,value));
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
