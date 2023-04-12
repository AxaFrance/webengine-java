package fr.axa.automation.webengine.constante;

import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public enum PredefinedTagValue {
    EMPTY ("empty"),
    NOT_EMPTY ("notEmpty"),
    CHECKED ("checked"),
    UNCHECKED ("unchecked"),
    DISPLAYED("displayed"),
    NOT_DISPLAYED("notDisplayed");


    final String tagValue;

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
