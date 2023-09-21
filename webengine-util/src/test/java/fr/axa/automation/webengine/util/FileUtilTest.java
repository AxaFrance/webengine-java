package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileUtilTest {

    public static final String TEST_CREATE_DIRECTORY = "test-create-directory";
    public static final Logger logger = LoggerFactory.getLogger(FileUtilTest .class);

    @Test
    public void testCreateDirectories() throws WebEngineException {
        String pathDirectoryToCreate = FileUtil.getPathTargetDirectory(Paths.get("").toAbsolutePath()) + File.separator + TEST_CREATE_DIRECTORY;
        Path path = FileUtil.createDirectories(pathDirectoryToCreate);
        logger.info("Create directory  : "+path.toAbsolutePath());
        Assertions.assertTrue(Files.exists(path));
    }

    @Test
    public void testSaveAsXMLWithoutNamespace() throws WebEngineException {
        saveAsXML("","");
    }

    @Test
    public void testSaveAsXMLWithNamespace() throws WebEngineException {
        saveAsXML("http://www.axa.fr/WebEngine/2022","ns");
    }

    private void saveAsXML(String namespace, String prefixe) throws WebEngineException {
        String pathResult;
        String fileName = "userObjectToXml.xml";
        Path xmlFilePath = Paths.get(FileUtil.createDirectoryInTarget(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST),fileName);
        pathResult = FileUtil.saveAsXml(getInputMarshallDTO(xmlFilePath, UserUtilForTest.getNewUserWithPackageInfo(), namespace, prefixe));

        Assertions.assertTrue(Files.exists(Paths.get(pathResult)));
    }

    private static InputMarshallDTO getInputMarshallDTO(Path xmlFilePath, Object objectToMarshall, String namespace, String prefix) {
        return InputMarshallDTO.builder().fileDestinationPath(xmlFilePath.toAbsolutePath().toString()).objectToMarshall(objectToMarshall).namespace(namespace).prefix(prefix).build();
    }

    @Test
    public void testCreateDirectoryInTmpDirectory() {
        File file = FileUtilForTest.createDirectoryInTmpDirectory("test-create-directory-in-tmp");
        logger.info("Create directory in temp directory : "+file.getAbsolutePath());
        Assertions.assertTrue(Files.exists(Paths.get(file.getAbsolutePath())));
    }

    @Test
    public void testCreateDirectoryInTargetDirectory() throws WebEngineException {
        String path = FileUtil.createDirectoryInTarget("test-create-directory-in-tmp");
        logger.info("Define directory in target directory : : "+path);
        Assertions.assertTrue(Files.exists(Paths.get(path)));
    }


    @Test
    public void testGetPathDirectoryInTargetDirectory() {
        String filePath = FileUtil.getPathInTargetDirectory("test-create-directory-in-target");
        logger.info("Define directory in target directory : "+ filePath);
        Assertions.assertTrue(filePath.contains(File.separator+"target"+File.separator));
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

    @Test
    void testGetInputStreamByResource() throws  IOException {
        InputStream inputStream = FileUtil.getInputStreamByPathOrResource("jaxb-users.xml");
        Assertions.assertNotNull(inputStream);
    }

    @Test
    void testGetInputStreamByPath() throws  IOException {
        String currentDirectoryPath = FileUtil.getCurrentPath();
        String completePathToFile = currentDirectoryPath + "src"+File.separator+"test"+File.separator+"resources"+File.separator+"jaxb-users.xml";
        logger.info("Current path : "+currentDirectoryPath);
        logger.info("Complete path to directory : "+completePathToFile);
        InputStream inputStream = FileUtil.getInputStreamByPathOrResource(completePathToFile);
        Assertions.assertNotNull(inputStream);
    }

    @Test
    void testAssertContentToFalse() throws URISyntaxException, IOException {
        boolean resultCompareFile = FileUtil.assertContent(FileUtil.getFileFromResource("xml"+File.separator+"user-with-custom-namespace.xml"), FileUtil.getFileFromResource("xml"+File.separator+"user-with-initial-namespace.xml"));
        Assertions.assertFalse(resultCompareFile);
    }

    @Test
    void testAssertContentToTrue() throws URISyntaxException, IOException {
        boolean resultCompareFile = FileUtil.assertContent(FileUtil.getFileFromResource("xml"+File.separator+"user-with-custom-namespace.xml"), FileUtil.getFileFromResource("xml"+File.separator+"user-with-custom-namespace.xml"));
        Assertions.assertTrue(resultCompareFile);
    }
}