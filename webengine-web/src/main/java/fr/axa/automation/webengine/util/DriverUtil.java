package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DriverUtil {


    public static File installDriver(String url, String directory, String zipName, String executableName) throws WebEngineException {
        InputStream inputStream = null;
        ZipFile zipFile = null;
        File driverFile = null;
        try {
            URL urlObject = new URL(url);
            inputStream = urlObject.openStream();
            File file = copyInToFile(directory + File.separator + zipName, inputStream);
            zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().contains(executableName)) {
                    inputStream = zipFile.getInputStream(entry);
                    driverFile = copyInToFile(directory + File.separator + executableName, inputStream);
                }
            }
        } catch (Exception e) {
            throw new WebEngineException("Error during installation driver", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                throw new WebEngineException("Error during installation driver", e);
            }
        }
        return driverFile;
    }


    private static File copyInToFile(String fileName, InputStream inputStream) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileUtils.copyInputStreamToFile(inputStream, file);
        return file;
    }

}
