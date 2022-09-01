package fr.axa.automation.webengine.util;


import java.awt.Robot;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;


/**
 *
 * @author  Jean-Prince DOTOU-SEGLA
 * @version 1.0
 * @since   2022-08-29
 */
public class ActiveWindowScreenShot {
    public static RenderedImage getGeneratedCurrentDesktopImage() {
        Robot robot = null;
        try {
            robot = new Robot();
            return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return new BufferedImage(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,Image.SCALE_DEFAULT);
    }
}

