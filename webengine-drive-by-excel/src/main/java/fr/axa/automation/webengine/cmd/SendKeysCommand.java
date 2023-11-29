package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;

public class SendKeysCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String value = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        executeActionInElement(value, commandData);
    }

    protected void executeActionInElement(String value, CommandDataNoCode cmdData)throws Exception {
        if (cmdData.getTargetList().isEmpty()){
            System.setProperty("java.awt.headless", "false");
            Robot robot = new Robot();
            if (value.startsWith("KEY_")) {
                robot.keyPress((Integer) KeyEvent.class.getField("VK_"+value.substring(4)).get(null));
            }
            else
            {
                StringSelection owner = new StringSelection(value);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(owner, owner);

                //imitate mouse events like ENTER, CTRL+C, CTRL+VRobot robot = new Robot();
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }
        }
        else {
            if (value.startsWith("Keys.")) {
                webElementDescription.sendKeyboard(StringUtils.substringAfterLast(value, "KEY_"));
            } else {
                webElementDescription.sendKeysWithAssertion(value);
            }
        }
    }

    private void writeString(Robot robot, String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.keyPress(Character.toUpperCase(c));
            robot.keyRelease(Character.toUpperCase(c));

            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        }
        robot.delay(500);
    }
}
