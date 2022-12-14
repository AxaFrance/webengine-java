package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.RenderedImage;

import static org.junit.jupiter.api.Assertions.*;

class ActiveWindowScreenShotUtilTest {

    @Test
    void testGetGeneratedCurrentDesktopImage() {
        RenderedImage renderedImage = ActiveWindowScreenShotUtil.getGeneratedCurrentDesktopImage();
        Assertions.assertNotNull(renderedImage);
    }

}