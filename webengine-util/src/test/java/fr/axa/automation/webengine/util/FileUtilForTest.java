package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.io.File;

public class FileUtilForTest {
    public static final String DIRECTORY_RESULT_UNIT_TEST = "result-unit-test";

    public static String getPathFileInTargetDirectory(String subDirectory, String fileName) throws WebEngineException {
        String path = FileUtil.createDirectoryInTarget(subDirectory);
        return path + File.separator + fileName;
    }

    public static File createDirectoryInTmpDirectory(String directoryName){
        final String dir = System.getProperty("java.io.tmpdir");
        File directory = new File(dir + File.separator + directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }
}
