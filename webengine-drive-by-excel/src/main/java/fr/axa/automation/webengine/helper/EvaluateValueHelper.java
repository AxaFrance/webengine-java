package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.constante.ConstantNoCode;
import fr.axa.automation.webengine.constante.PredefinedDateTagValue;
import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractSettings;
import fr.axa.automation.webengine.global.SettingsNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FormatDate;
import fr.axa.automation.webengine.util.RegexUtil;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EvaluateValueHelper {

    public static String getInternalValue(String value){
        return StringUtils.substringBetween(value, ConstantNoCode.INTERNAL_PREFIX.getValue(), ConstantNoCode.INTERNAL_SUFFIX.getValue());
    }

    public static String getExternalValue(String value){
        return StringUtils.substringBetween(value, ConstantNoCode.EXTERNAL_PREFIX.getValue(), ConstantNoCode.EXTERNAL_SUFFIX.getValue());
    }

    public static String getKeyboardValue(String value){
        return StringUtils.substringBetween(value, ConstantNoCode.KEYBOARD_VALUE_PREFIX.getValue(), ConstantNoCode.KEYBOARD_VALUE_SUFFIX.getValue());
    }



    public static String evaluateValue(AbstractSettings settings, String completeValue, List<CommandResult> commandResultList) throws WebEngineException{
        List<String> externalRegexValueList = RegexUtil.match(RegexContante.EXTERNAL_REGEX_VALUE, completeValue);
        List<String> internalRegexValueList = RegexUtil.match(RegexContante.INTERNAL_REGEX_VALUE, completeValue);
        List<String> keyboardRegexValueList = RegexUtil.match(RegexContante.KEYBOARD_REGEX_VALUE, completeValue);

        String evaluateValue = completeValue;
        if(CollectionUtils.isNotEmpty(externalRegexValueList)){
            evaluateValue = evaluateExternalRegexValue(completeValue, externalRegexValueList, settings);
        }
        if(CollectionUtils.isNotEmpty(internalRegexValueList)){
            evaluateValue = evaluateInternalRegexValue(evaluateValue, internalRegexValueList, commandResultList);
        }
        if(CollectionUtils.isNotEmpty(keyboardRegexValueList)){
            evaluateValue = evaluateKeyboardRegexValue(evaluateValue,keyboardRegexValueList);
        }
        return evaluateValue;
    }

    public static String evaluateKeyboardRegexValue(String completeValue, List<String> keyBoardRegexValueList) {
        String resultValue = completeValue;
        if (CollectionUtils.isNotEmpty(keyBoardRegexValueList)) {
            for (String keyBoardValue : keyBoardRegexValueList) {
                return getKeyboardValue(keyBoardValue);
            }
        }
        return resultValue;
    }

    public static String evaluateExternalRegexValue(String completeValue, List<String> externalRegexValueList, AbstractSettings settings) throws WebEngineException {
        String resultValue = completeValue;
        if (CollectionUtils.isNotEmpty(externalRegexValueList)) {
            for (String externalRegexValue : externalRegexValueList) {
                String valueWithouBrackets = getExternalValue(externalRegexValue);
                String valueFromSetting = settings.getValues().get(valueWithouBrackets);
                if (StringUtils.isEmpty(valueFromSetting)) {
                    //Check the value in Keepass if the value is not found in the settings
                    resultValue = KeepassUtils.getPassword(valueWithouBrackets, ((SettingsNoCode)settings).getKeePassDatabasePassword(), ((SettingsNoCode)settings).getKeePassDatabasePath());
                    if(StringUtils.isEmpty(resultValue)){
                        throw  new WebEngineException("The keepass password can't be empty");
                    }
                }else {
                    resultValue = resultValue.replace(externalRegexValue, valueFromSetting);
                }
            }
        }
        return resultValue;
    }

    private static String evaluateInternalRegexValue(String completeValue, List<String> internalRegexValueList, List<CommandResult> commandResultList ) {
        String resultValue = completeValue;
        if(CollectionUtils.isNotEmpty(internalRegexValueList)){
            for (String referencedRegexValue: internalRegexValueList) {
                String valueWithouRafter = getInternalValue(referencedRegexValue);
                if(PredefinedDateTagValue.isContainsPredefinedDateTagValue(valueWithouRafter)) {
                    resultValue = resultValue.replace(referencedRegexValue,replaceTagDateValue(valueWithouRafter));
                } else if (PredefinedTagValue.isContainsPredefinedTagValue(valueWithouRafter)) {
                    resultValue = resultValue.replace(referencedRegexValue,PredefinedTagValue.valueOf(valueWithouRafter).getTagValue());
                } else if (isContainsInternalValue(valueWithouRafter, commandResultList)) {
                    String savedData = getSavedData(valueWithouRafter,commandResultList);
                    resultValue = resultValue.replace(referencedRegexValue,savedData);
                }
            }
        }
        return resultValue;
    }

    public static boolean isContainsInternalValue(String value, List<CommandResult> commandResultList){
        if(CollectionUtils.isNotEmpty(commandResultList)){
            List<CommandResult> commandResultFlatList = CommandResultHelper.flatCommandResult(commandResultList,new ArrayList<>());
            return commandResultFlatList.stream().anyMatch(commandResult -> StringUtil.equalsIgnoreCase(commandResult.getCommandData().getName(),value));
        }
        return false;
    }

    public static String getSavedData(String value, List<CommandResult> commandResultList){
        if(CollectionUtils.isNotEmpty(commandResultList)){
            List<CommandResult> commandResultFlatList = CommandResultHelper.flatCommandResult(commandResultList,new ArrayList<>());
            List<String> saveDataList = commandResultFlatList.stream()
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
        if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_TODAY.getTagValue()) && value.contains(ConstantNoCode.MINUS.getValue())) {
            return DateUtil.minusDay(FormatDate.DDMMYYYY,RegexUtil.getNumber(RegexContante.REGEX_NUMBER,value));
        } else if (StringUtils.equalsIgnoreCase(onlyTagValue, PredefinedDateTagValue.TAG_TODAY.getTagValue()) && value.contains(ConstantNoCode.PLUS.getValue())) {
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
