package fr.axa.automation.webengine.constante;

import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public enum PredefinedTagValue {
    EXISTS ("exist"),
    NOT_EXISTS ("notExist"),
    EMPTY ("empty"),
    NOT_EMPTY ("notEmpty"),
    CHECKED ("checked"),
    NOT_CHECKED("notChecked"),
    DISPLAYED("displayed"),
    NOT_DISPLAYED("notDisplayed");

    final String tagValue;

    public static PredefinedTagValue fromTagValue(String tagValue) {
        for (PredefinedTagValue predefinedTagValue : PredefinedTagValue.values()) {
            if (predefinedTagValue.getTagValue().equalsIgnoreCase(tagValue)) {
                return predefinedTagValue;
            }
        }
        throw new IllegalArgumentException(tagValue);
    }

    public static List<String> getTagValueList() {
        return Arrays.asList(PredefinedTagValue.values()).stream().map(predefinedTagValue -> predefinedTagValue.getTagValue()).collect(Collectors.toList());
    }

    public static boolean isContainsPredefinedTagValue(String value){
        List<String> list = getTagValueList().stream().filter(predifinedTagValue -> StringUtil.equalsIgnoreCase(value,predifinedTagValue)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(list);
    }
}
