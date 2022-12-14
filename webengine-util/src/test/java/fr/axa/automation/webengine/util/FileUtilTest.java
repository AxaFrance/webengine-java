package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileUtilTest {

    public static final String TEST_CREATE_DIRECTORY = "test-create-directory";
    static final Logger logger = LoggerFactory.getLogger(FileUtilTest .class);

    @Test
    public void testCreateDirectories() throws WebEngineException {
        String pathDirectoryToCreate = FileUtil.getPathTargetDirectory(Paths.get("").toAbsolutePath()) + File.separator + TEST_CREATE_DIRECTORY;
        Path path = FileUtil.createDirectories(pathDirectoryToCreate);
        logger.info("Create directory  : "+path.toAbsolutePath());
        Assertions.assertTrue(Files.exists(path));
    }

    @Test
    public void testSaveAsXMLWithoutNamespace() throws WebEngineException {
        saveAsXML(null,null);
    }

    @Test
    public void testSaveAsXMLWithNamespace() throws WebEngineException {
        saveAsXML("http://www.axa.fr/WebEngine/2022","ns");
    }

    private void saveAsXML(String namespace, String prefixe) throws WebEngineException {
        String path;
        if(StringUtils.isEmpty(namespace) && StringUtils.isEmpty(prefixe)){
            path = FileUtil.saveAsXml(FileUtilForTest.createDirectoryInTarget(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST),"userObjectToXml",UserUtilForTest.getNewUserTest());
        }else{
            path = FileUtil.saveAsXml(FileUtilForTest.createDirectoryInTarget(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST),"userObjectToXml",UserUtilForTest.getNewUserTest(),namespace,prefixe);
        }
        Assertions.assertTrue(Files.exists(Paths.get(path)));
    }

    @Test
    public void testCreateDirectoryInTmpDirectory() {
        File file = FileUtil.createDirectoryInTmpDirectory("test-create-directory");
        logger.info("Create directory in temp directory : "+file.getAbsolutePath());
        Assertions.assertTrue(Files.exists(Paths.get(file.getAbsolutePath())));
    }

    @Test
    public void testCreateDirectoryInTargetDirectory() {
        String filePath = FileUtil.createDirectoryInTargetDirectory("test-create-directory");
        logger.info("Create directory in target directory : "+ filePath);
        Assertions.assertTrue(Files.exists(Paths.get(filePath)));
    }

    @Test
    public void testGetPathTargetDirectory() {
        String targerDir = FileUtil.getPathTargetDirectory(Paths.get("").toAbsolutePath()) ;
        logger.info("Get target directory : "+ targerDir);
        Assertions.assertTrue(targerDir.contains("target"));
    }

    @Test
    void testIsTargetDirectory() {
        Path currentPath = Paths.get("").toAbsolutePath();
        boolean isTargerDir = FileUtil.isTargetDirectory(currentPath) ;
        logger.info("Current path : "+ currentPath);
        logger.info("Is target directory : "+ isTargerDir);
        Assertions.assertFalse(isTargerDir);
    }

    @Test
    void testGetFileFromResource() throws URISyntaxException {
        File file = FileUtil.getFileFromResource("jaxb-users.xml");
        logger.info("Get file from resource : "+file.getAbsolutePath());
        Assertions.assertTrue(Files.exists(Paths.get(file.getAbsolutePath())));
    }
}