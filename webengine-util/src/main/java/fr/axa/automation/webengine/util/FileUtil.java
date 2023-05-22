package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.stream.Stream;

public final class FileUtil {

    private FileUtil() {
    }

    public static final String TARGET_DIRECTORY = "target";
    public static final String RUN_RESULT_DIRECTORY = "report-test-result";

    public static final ILoggerService loggerService = LoggerServiceProvider.getInstance();

    public static Path createDirectories(String path) throws WebEngineException {
        try {
            return Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new WebEngineException("Error during creating directory : " + path,e);
        }
    }

    public static String saveAsXml(InputMarshallDTO inputMarshallDTO) throws WebEngineException {
        return XmlUtil.marshall(inputMarshallDTO).getAbsolutePath();
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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("The file "+fileName+" not found in resource directory ");
        } else {
            return new File(resource.toURI());
        }
    }

    public static boolean assertContent(File fileContentExpected, File fileContentResult) throws IOException {
        try(Reader reader1 = new BufferedReader(new FileReader(fileContentExpected));
            Reader reader2 = new BufferedReader(new FileReader(fileContentResult))){
            return IOUtils.contentEqualsIgnoreEOL(reader1, reader2);
        }
    }

    public static InputStream getInputStreamByPathOrResource(String fileOrResource) throws IOException {
        try {
            return new FileInputStream(fileOrResource);
        } catch (FileNotFoundException fileNotFoundException) {
            return getInputStreamFromResource(fileOrResource);
        }
    }

    public static File getFileByPathOrResource(String fileOrResource) throws IOException {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(fileOrResource);
            return url != null ? new File(url.toURI()) : new File(fileOrResource);
        }catch (Exception e){
            return new File(fileOrResource);
        }
    }

    public static InputStream getInputStreamFromResource(String resourceName) throws FileNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceName);
        if(inputStream==null){
            throw new FileNotFoundException("The resource file "+resourceName+" not found in resource directory ");
        }
        return inputStream;
    }

    public static String getCurrentPath() throws IOException {
        File currentDirFile = new File(".");
        String path = currentDirFile.getAbsolutePath();
        return path.substring(0, path.length() - 1);
    }

    public static void displayContent(String fileName){
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(loggerService::info);
        } catch (IOException e) {
            loggerService.error("Error during displaying content of file",e);
        }
    }
}


