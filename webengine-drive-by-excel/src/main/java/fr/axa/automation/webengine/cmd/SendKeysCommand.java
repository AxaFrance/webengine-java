package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.RobotHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SendKeysCommand extends AbstractDriverCommand {

    public static final String KEY_ = "KEY_";

    private final RobotHelper robotHelper = new RobotHelper();

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        webElementDescription = populateWebElement(globalApplicationContext, testCaseContext, commandData, commandResultList);
        String value = getValue(globalApplicationContext, (TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        executeActionInElement(value, commandData);
    }

    protected void executeActionInElement(String value, CommandDataNoCode cmdData) throws Exception {
        if (cmdData.getTargetList().isEmpty()) {
            robotHelper.sendKeys(value);
        } else {
            if (value.startsWith(KEY_)) {
                webElementDescription.sendKeyboard(StringUtils.substringAfterLast(value, KEY_));
            } else {
                webElementDescription.sendKeysWithAssertion(value);
            }
        }
    }
}
