package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageUtilTest {

    @Test
    void testGetImage() {
        byte[] screenshot = ImageUtil.getImage(ActiveWindowScreenShotUtil.getGeneratedCurrentDesktopImage());
        Assertions.assertNotNull(screenshot);
    }
}