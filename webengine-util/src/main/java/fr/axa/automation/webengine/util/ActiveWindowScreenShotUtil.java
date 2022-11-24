package fr.axa.automation.webengine.util;


import lombok.extern.slf4j.Slf4j;

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

@Slf4j
public class ActiveWindowScreenShotUtil {
    public static RenderedImage getGeneratedCurrentDesktopImage() {
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            log.error("Error lors de la prise du screenshot",e);
        }
        return new BufferedImage(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,Image.SCALE_DEFAULT);
    }
}

