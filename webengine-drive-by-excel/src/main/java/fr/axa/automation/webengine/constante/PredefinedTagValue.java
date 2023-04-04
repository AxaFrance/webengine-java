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

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum PredefinedTagValue {
    TAG_TODAY ("today"),
    TAG_TODAY_HOUR ("today_hour"),
    TAG_YESTERDAY("yesterday"),
    TAG_PAST_DAY("pastday"),
    TAG_ANTERIOR_DAY("anteriorday"),
    TAG_NEXT_DAY("nextday"),
    TAG_NEXT_MONTH("nextmonth"),
    TAG_ANTERIOR_MONTH("anteriormonth");

    final String tagValue;

    public static PredefinedTagValue fromTagValue(String v) {
        for (PredefinedTagValue predefinedTagValue: PredefinedTagValue.values()) {
            if (predefinedTagValue.getTagValue().equalsIgnoreCase(v)) {
                return predefinedTagValue;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static List<String> getTagValueList() {
        List<String> tagValueList = new ArrayList<>();
        for (PredefinedTagValue predefinedTagValue: PredefinedTagValue.values()) {
            tagValueList.add(predefinedTagValue.getTagValue());
        }
        return tagValueList;
    }

    public static boolean isContainsPredefinedTagValue(String value){
        List<String> list = getTagValueList().stream().filter(predifinedTagValue -> StringUtil.contains(value,predifinedTagValue)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(list);
    }
}
