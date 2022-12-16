package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.xml.NamespacePrefixerWebengine;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class FileUtil {

    public static final String TARGET_DIRECTORY = "target";
    public static final String RUN_RESULT_DIRECTORY = "report-test-result";

    public static Path createDirectories(String path) throws WebEngineException {
        try {
            return Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new WebEngineException("Error during creating directory : " + path,e);
        }
    }

    public static String saveAsXml(Path filePath, Object object) throws WebEngineException {
        return saveAsXml(filePath,object,"","");
    }

    public static String saveAsXml(Path filePath, Object object,String namespace, String prefixe) throws WebEngineException {
        Map<String, String> namespaceAndPrefixMap  = new HashMap() {{put(namespace, prefixe);}};
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath.toAbsolutePath().toString()).objectToMarshall(object).namespaceRoot(namespace).namespacePrefixMapper(new NamespacePrefixerWebengine(namespaceAndPrefixMap)).build();
        return XmlUtil.marshall(inputMarshallDTO).getAbsolutePath();
    }

    public static File createDirectoryInTmpDirectory(String directoryName){
        final String dir = System.getProperty("java.io.tmpdir");
        File directory = new File(dir + File.separator + directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static String createDirectoryInTarget(String subDirectory) throws WebEngineException {
        return FileUtil.createDirectories(FileUtil.getPathInTargetDirectory(subDirectory)).toAbsolutePath().toString();
    }

    public static String getPathInTargetDirectory(String directoryToCreate){
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

    public static boolean assertContent(File fileContentExpected, File fileContentResult) throws IOException {
        Reader reader1 = new BufferedReader(new FileReader(fileContentExpected));
        Reader reader2 = new BufferedReader(new FileReader(fileContentResult));
        return IOUtils.contentEqualsIgnoreEOL(reader1, reader2);
    }
}


