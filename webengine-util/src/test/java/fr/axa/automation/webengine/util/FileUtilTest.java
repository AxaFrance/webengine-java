package fr.axa.automation.webengine.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testCopyFileFromResource() throws IOException {
        // Call the method under test
        FileUtil.copyFileFromResource("copy-source.txt", "target/copy.txt");
        assertEquals("TEST", Files.readString(Paths.get("target/copy.txt")));
        Files.delete(Paths.get("target/copy.txt"));
    }

    @Test
    void testGetResourceFiles() throws IOException {
        // Call the method under test
        List<String> resultContent = FileUtil.getResourceFiles("xml");

        // Check that the result content is the same as the source content
        Assertions.assertTrue(resultContent.contains("user-with-custom-namespace.xml"));
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
        String filePath = FileUtil.getPathWithTargetDirectory("test-create-directory-in-target");
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


    @Test
    void testSaveObjectAsYamlFile() throws IOException {
        // Create a simple object
        Object object = new Object() {
            public String field1 = "Hello";
            public String field2 = "World";
        };

        // Create a temporary file
        Path tempFilePath = Files.createTempFile("temp", ".yaml");

        // Call the method under test
        FileUtil.saveObjectAsYamlFile(object, tempFilePath.toString());

        // Read the content of the file
        String fileContent = Files.readString(tempFilePath);

        // Create an ObjectMapper for comparison
        YAMLFactory yamlFactory = YAMLFactory.builder().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER).build();
        ObjectMapper mapper = new ObjectMapper(yamlFactory);

        // Check that the file content is the same as the object
        assertEquals(mapper.writeValueAsString(object).trim(), fileContent.trim());

        // Clean up the temporary file
        Files.delete(tempFilePath);
    }
}