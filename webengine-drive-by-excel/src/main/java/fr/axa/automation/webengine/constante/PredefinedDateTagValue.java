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
public enum PredefinedDateTagValue implements IPredefinedTag{
    TAG_TODAY ("today"),
    TAG_TODAY_HOUR ("today_hour"),
    TAG_YESTERDAY("yesterday"),
    TAG_PAST_DAY("pastday"),
    TAG_ANTERIOR_DAY("anteriorday"),
    TAG_NEXT_DAY("nextday"),
    TAG_NEXT_MONTH("nextmonth"),
    TAG_ANTERIOR_MONTH("anteriormonth");

    final String tagValue;

    public static PredefinedDateTagValue fromTagValue(String v) {
        for (PredefinedDateTagValue predefinedDateTagValue : PredefinedDateTagValue.values()) {
            if (predefinedDateTagValue.getTagValue().equalsIgnoreCase(v)) {
                return predefinedDateTagValue;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static List<String> getDateTagValueList() {
        List<String> tagValueList = new ArrayList<>();
        for (PredefinedDateTagValue predefinedDateTagValue : PredefinedDateTagValue.values()) {
            tagValueList.add(predefinedDateTagValue.getTagValue());
        }
        return tagValueList;
    }

    public static boolean isContainsPredefinedDateTagValue(String value){
        List<String> list = getDateTagValueList().stream().filter(predifinedDateTagValue -> StringUtil.contains(value,predifinedDateTagValue)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(list);
    }
}
