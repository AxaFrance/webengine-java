package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static void encodeToFile(Object object, String fileName) throws IOException {
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(fileName))) {
            encoder.writeObject(object);
            encoder.flush();
        }
    }

    public static Path createDirectories(String path) throws IOException {
        return Files.createDirectories(Paths.get(path));
    }

    public static String saveAsXML(String path, String fileName, Object object) throws IOException, WebEngineException {
        Path filePath = Paths.get(path,fileName);
        XmlUtil.marshallWithoutNamespace(filePath.toString(),object);
        return filePath.toString();
    }

    public static String saveAsXml(String path, String fileName, Object object,String namespace, String prefixe) throws IOException, WebEngineException {
        Path filePath = Paths.get(path,fileName);
        XmlUtil.marshallWithNamespace(filePath.toString(),object,namespace,prefixe);
        return filePath.toString();
    }

    public static File createDirectoryInUserDir(String directoryName){
        final String dir = System.getProperty("java.io.tmpdir");
        File directory = new File(dir + directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

}


