package fr.axa.automation.webengine.constante;

import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface IPredefinedTag {

    public static PredefinedTagValue fromTagValue(String v) {
        for (PredefinedTagValue predefinedTagDateValue : PredefinedTagValue.values()) {
            if (predefinedTagDateValue.getTagValue().equalsIgnoreCase(v)) {
                return predefinedTagDateValue;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static List<String> getTagValueList() {
        List<String> tagValueList = new ArrayList<>();
        for (PredefinedTagValue predefinedTagDateValue : PredefinedTagValue.values()) {
            tagValueList.add(predefinedTagDateValue.getTagValue());
        }
        return tagValueList;
    }

    public static boolean isContainsPredefinedTagValue(String value){
        List<String> list = getTagValueList().stream().filter(predifinedTagValue -> StringUtil.contains(value,predifinedTagValue)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(list);
    }
}
