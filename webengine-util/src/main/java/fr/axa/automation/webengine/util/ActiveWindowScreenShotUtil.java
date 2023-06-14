package fr.axa.automation.webengine.util;


import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;


/**
 *
 * @author  Jean-Prince DOTOU-SEGLA
 * @version 1.0
 * @since   2022-08-29
 */

@Slf4j
public final class ActiveWindowScreenShotUtil {

    private ActiveWindowScreenShotUtil() {
    }

    public static RenderedImage getGeneratedCurrentDesktopImage() {
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            return new BufferedImage(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,Image.SCALE_DEFAULT);
        }
    }
}

