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
    TIMESTAMP("timestamp"),
    TAG_TODAY ("today"),
    TAG_TODAY_HOUR ("today_hour"),
    TAG_YESTERDAY("yesterday"),
    TAG_PAST_DAY("past_day"),
    TAG_ANTERIOR_DAY("anterior_day"),
    TAG_NEXT_DAY("next_day"),
    TAG_NEXT_MONTH("next_month"),
    TAG_ANTERIOR_MONTH("anterior_month");

    final String tagValue;

    public static PredefinedDateTagValue fromTagValue(String tagValue) {
        for (PredefinedDateTagValue predefinedDateTagValue : PredefinedDateTagValue.values()) {
            if (predefinedDateTagValue.getTagValue().equalsIgnoreCase(tagValue)) {
                return predefinedDateTagValue;
            }
        }
        throw new IllegalArgumentException(tagValue);
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
