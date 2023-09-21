package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.ConstantNoCode;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssertContentCommand extends AbstractDriverCommand {

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        webElementDescription = populateWebElement(globalApplicationContext, testCaseContext, commandData, commandResultList);
        String expected = getValue(globalApplicationContext, (TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        Map<String, List<String>> contentMap = webElementDescription.getContentByElementType(expected);

        Map<String, List<String>> filterContentMap = contentMap.entrySet().stream()
                .filter(entry -> entry.getValue().contains(expected) || entry.getKey().contains(expected))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (MapUtils.isEmpty(filterContentMap)) {
            String errorMessage = "The expected value is : '" + expected + "'";
            getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(errorMessage);
            errorMessage = "The actual contentMap is : '" + StringUtils.substringBetween(contentMap.values().toString(), "[[", "]]")   +"'";
            getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(errorMessage);
            throw new WebEngineException(errorMessage);
        }
    }
}

