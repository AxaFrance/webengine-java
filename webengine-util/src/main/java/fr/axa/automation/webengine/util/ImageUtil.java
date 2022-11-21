package fr.axa.automation.webengine.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ImageUtil {

    public static final String JPG = "jpg";

    public static byte[] getImage(RenderedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, JPG, baos);
        } catch (IOException e) {
            log.error("Error de l'écriture de l'image");
        }
        return baos.toByteArray();
    }
}
