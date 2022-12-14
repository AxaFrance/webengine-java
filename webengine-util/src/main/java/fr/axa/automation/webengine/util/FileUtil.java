package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

public class FileUtil {

    public static final String TARGET_DIRECTORY = "target";
    public static final String RUN_RESULT_DIRECTORY = "run-result-";

    public static Path createDirectories(String path) throws WebEngineException {
        try {
            return Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new WebEngineException("Error during creating directory : " + path,e);
        }
    }

    public static String saveAsXml(String path, String fileName, Object object) throws WebEngineException {
        Path filePath = Paths.get(path,fileName);
        XmlUtil.marshallWithoutNamespace(filePath.toString(),object);
        return filePath.toString();
    }

    public static String saveAsXml(String path, String fileName, Object object,String namespace, String prefixe) throws WebEngineException {
        Path filePath = Paths.get(path,fileName);
        XmlUtil.marshallWithNamespace(filePath.toString(),object,namespace,prefixe);
        return filePath.toString();
    }

    public static File createDirectoryInTmpDirectory(String directoryName){
        final String dir = System.getProperty("java.io.tmpdir");
        File directory = new File(dir + File.separator + directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static String createDirectoryInTargetDirectory(String directoryToCreate){
        StringJoiner directory = new StringJoiner(File.separator);
        Path currentAbsolutePath = Paths.get("").toAbsolutePath();
        directory.add(currentAbsolutePath.toString()).add(getPathTargetDirectory(currentAbsolutePath)).add(directoryToCreate);
        return directory.toString();
    }

    public static String getPathTargetDirectory(Path path) {
        return isTargetDirectory(path) ? "" : TARGET_DIRECTORY;
    }

    public static boolean isTargetDirectory(Path currentAbsolutePath) {
        return currentAbsolutePath.getParent().toString().equalsIgnoreCase(TARGET_DIRECTORY);
    }

    public static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = FileUtil.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }
}


