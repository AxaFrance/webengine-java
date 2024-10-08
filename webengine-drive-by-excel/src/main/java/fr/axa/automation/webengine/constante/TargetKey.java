package fr.axa.automation.webengine.constante;

import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.util.RegexUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public enum TargetKey {
    CALL, OPEN, ID, XPATH, COMBINAISON_OF_LOCATOR, EXPRESSION_TO_EVALUATE;

    public static Map.Entry<TargetKey, String> getTargetValueList(CommandName commandName, String targetCellValue) {
        if(StringUtils.isEmpty(targetCellValue)){
            return new AbstractMap.SimpleEntry(null,null);
        }
        List<String> internalRegexValueList = RegexUtil.match(RegexContante.INTERNAL_REGEX_VALUE, targetCellValue);
        if (CommandName.CALL == commandName) {
            return new AbstractMap.SimpleEntry(CALL, targetCellValue);
        }else if (CollectionUtils.isNotEmpty(RegexUtil.match(TargetKeyPattern.XPATH_PATTERN, targetCellValue))) {
            return new AbstractMap.SimpleEntry(XPATH, targetCellValue);
        } else if (CollectionUtils.isNotEmpty(RegexUtil.match(TargetKeyPattern.JSON_PATTERN, targetCellValue))) {
            return new AbstractMap.SimpleEntry(COMBINAISON_OF_LOCATOR, targetCellValue);
        }else if(CollectionUtils.isNotEmpty(internalRegexValueList)){
            return new AbstractMap.SimpleEntry(EXPRESSION_TO_EVALUATE, targetCellValue);
        }else {
            return new AbstractMap.SimpleEntry(ID, targetCellValue);
        }
    }
}
