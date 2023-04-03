package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.constante.RegexContante;
import fr.axa.automation.webengine.util.DateUtil;
import fr.axa.automation.webengine.util.FormatDate;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class EvaluateValue {

    public static List<String> getValue(String completeValue){
        List<String> resultValue = new ArrayList<>();
//        List<String> regexValueList = RegexUtil.match(RegexContante.REFERENCED_VALUE_REGEX, completeValue);
//        if (CollectionUtils.isNotEmpty(regexValueList)) {
//            for (String regexValue:regexValueList) {
//                if(isTagDateValue(regexValue)){
//                    resultValue.add(replaceTagDateValue(regexValue));
//                } else if () {
//
//                }else{
//                    resultValue.add(completeValue);
//                }
//            }
//        }
        return resultValue;
    }

    public static boolean isTagDateValue(String value){
        return PredefinedTagValue.getTagValueList().contains(value);
    }

    private static String replaceTagDateValue(String key){
        if (PredefinedTagValue.TAG_TODAY.getTagValue().equalsIgnoreCase(key)) {
            return DateUtil.getDateTime(FormatDate.DDMMYYYY.getFormat());
        } else if (PredefinedTagValue.TAG_TODAY_HOUR.getTagValue().equalsIgnoreCase(key)) {
            return DateUtil.getDateTime(FormatDate.DDMMYYYYHHMM.getFormat());
        } else if (PredefinedTagValue.TAG_ANTERIOR_DAY.getTagValue().equalsIgnoreCase(key) || PredefinedTagValue.TAG_YESTERDAY.getTagValue().equalsIgnoreCase(key)) {
            return DateUtil.minusDay(FormatDate.DDMMYYYY,1L);
        } else if (PredefinedTagValue.TAG_NEXT_DAY.getTagValue().equalsIgnoreCase(key) || PredefinedTagValue.TAG_PAST_DAY.getTagValue().equalsIgnoreCase(key)) {
            return DateUtil.addDay(FormatDate.DDMMYYYY,1L);
        } else if (PredefinedTagValue.TAG_NEXT_MONTH.getTagValue().equalsIgnoreCase(key)) {
            return DateUtil.addMonth(FormatDate.DDMMYYYY,1L);
        }else if (PredefinedTagValue.TAG_NEXT_MONTH.getTagValue().equalsIgnoreCase(key)) {
            return DateUtil.addMonth(FormatDate.DDMMYYYY,1L);
        } else {
            return key;
        }
    }
}
