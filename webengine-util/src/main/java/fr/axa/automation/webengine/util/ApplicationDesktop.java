package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ApplicationDesktop {

    static {
        System.setProperty("java.awt.headless", "false");
    }

    public static void openDefaultBrowser(String path) throws WebEngineException {
        try {
            File htmlFile = new File(path);
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException e) {
            throw new WebEngineException("Error during opening report", e);
        }
    }

    public static void openFile(String path) throws WebEngineException {
        try {
            File htmlFile = new File(path);
            Desktop.getDesktop().open(htmlFile);
        } catch (IOException e) {
            throw new WebEngineException("Error during opening file", e);
        }
    }
}
