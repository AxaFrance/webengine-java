package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.HtmlAttributeConstante;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.SettingsDriveByExcel;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;
import java.util.List;

public class UploadFileCommand extends AbstractDriverCommand {
    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList) throws Exception {
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext,commandData,commandResultList);
        String value = getValue(globalApplicationContext,(TestCaseDriveByExcelContext) testCaseContext, commandData, commandResultList);
        webElementDescription.click();
        Thread.sleep(1000);

        String filepath =  ((SettingsDriveByExcel)globalApplicationContext.getSettings()).getDataTestFileName();
        filepath = Paths.get(filepath).getParent().toString();
        filepath = filepath.replace("/","\\");
        StringSelection owner = new StringSelection(filepath+ "\\Upload\\"+value);
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
