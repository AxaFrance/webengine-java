package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.cmd.SendKeysCommand;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class RobotHelper {
    static {
        System.setProperty("java.awt.headless", "false");
    }
    public RobotHelper() {
    }

    public void sendKeys(String value) throws AWTException, IllegalAccessException, NoSuchFieldException {
        Robot robot = new Robot();
        if (value.startsWith(SendKeysCommand.KEY_)) {
            robot.keyPress((Integer) KeyEvent.class.getField("VK_" + value.substring(4)).get(null));
            robot.delay(500);
        } else {
            StringSelection owner = new StringSelection(value);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(owner, owner);

            //imitate mouse events like ENTER, CTRL+C, CTRL+VRobot robot = new Robot();
            robot.delay(500);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(500);
        }
    }

    public void writeString(Robot robot, String s) {
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