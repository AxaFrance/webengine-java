package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.SettingsNoCode;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class UploadFileCommand extends AbstractDriverCommand {
    static { /* works fine! ! */
            System.setProperty("java.awt.headless", "false");
    }

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String value = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        webElementDescription.focusAndClickFromActions();
        Thread.sleep(1000);
        uploadLikeImatatingMouse(globalApplicationContext, value);
    }

    private String getFilePath(AbstractGlobalApplicationContext globalApplicationContext, String value) {
        if (Files.exists(Paths.get(value))){
            return value;
        }else {
            String filepath = ((SettingsNoCode) globalApplicationContext.getSettings()).getDataTestFileName();
            return Paths.get(filepath).getParent().toString() + File.separator + "Upload" + File.separator + value;
        }
    }

    private void uploadLikeImatatingMouse(AbstractGlobalApplicationContext globalApplicationContext, String value) throws AWTException {
        StringSelection owner = new StringSelection(getFilePath(globalApplicationContext, value));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(owner,owner);
        //owner.lostOwnership(Toolkit.getDefaultToolkit().getSystemClipboard(),owner);

        //imitate mouse events like ENTER, CTRL+C, CTRL+VRobot robot = new Robot();
        Robot robot = new Robot();
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(90);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}
