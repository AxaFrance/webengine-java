package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilTest {

    @Test
    void getImage() {
        byte[] screenshot = ImageUtil.getImage(ActiveWindowScreenShotUtil.getGeneratedCurrentDesktopImage());
        Assertions.assertNotNull(screenshot);
    }
}