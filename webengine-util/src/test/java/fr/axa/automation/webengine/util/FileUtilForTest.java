package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.io.File;

public class FileUtilForTest {
    public static final String DIRECTORY_RESULT_UNIT_TEST = "result-unit-test";

    public static String createFileInTargetDirectory(String subDirectory,String fileName) throws WebEngineException {
        String path = FileUtil.createDirectoryInTarget(subDirectory);
        return new File(path + File.separator + fileName).getAbsolutePath();
    }


}
