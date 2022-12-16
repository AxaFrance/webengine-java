package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.jaxb.withoutpackageinfo.UserWithoutPackageInfo;
import fr.axa.automation.webengine.jaxb.withpackageinfo.UserWithPackageInfo;
import fr.axa.automation.webengine.xml.NamespacePrefixerWebengine;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class XmlUtilTest {

    private static UserWithPackageInfo getUserFromXml(String filePath) throws URISyntaxException, WebEngineException {
        return XmlUtil.unmarshall(filePath, UserWithPackageInfo.class);
    }

    @Test
    public void testUnmarshall() throws URISyntaxException, WebEngineException {
        String xmlFilePath = FileUtil.getFileFromResource("jaxb-users.xml").getAbsolutePath();
        UserWithPackageInfo user = getUserFromXml(xmlFilePath);
        Assertions.assertEquals(1,user.getId());
        Assertions.assertEquals("Ramesh",user.getFirstName());
    }
    @Test
    public void testMarshallWithoutNamespace1() throws WebEngineException, IOException {
        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><userwithoutpackageinfoid=\"1\"><firstName>Ramesh</firstName><lastName>Fadatare</lastName><age>25</age><gender>Male</gender></userwithoutpackageinfo>";
        UserWithoutPackageInfo user = UserUtilForTest.getNewUserWithoutPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-without-namespace.xml");
        File fileResult = XmlUtil.marshallWithoutNamespace(filePath, user);
        assertContentXml(fileContentExpected, fileResult);
    }

    @Test
    public void testMarshallWitNamespace() throws WebEngineException, IOException {
        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><ns:UserWithPackageInfoxmlns:ns=\"http://www.axa.fr/WebEngine/2022\"id=\"1\"><ns:firstName>Ramesh</ns:firstName><ns:lastName>Fadatare</ns:lastName><ns:age>25</ns:age><ns:gender>Male</ns:gender></ns:UserWithPackageInfo>";
        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-with-namespace.xml");
        File fileResult = XmlUtil.marshallWithNamespace(filePath, user,"http://www.axa.fr/WebEngine/2022","ns");
        assertContentXml(fileContentExpected,fileResult);
    }

    @Test
    public void testMarshallWithoutNamespaceAndWithoutPackageInfo() throws WebEngineException, IOException {
        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><UserWithoutPackageInfoid=\"1\"><firstName>Ramesh</firstName><lastName>Fadatare</lastName><age>25</age><gender>Male</gender></UserWithoutPackageInfo>";
        UserWithoutPackageInfo user = UserUtilForTest.getNewUserWithoutPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-with-namespace.xml");
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        assertContentXml(fileContentExpected, fileResult);
    }



    @Test
    public void testMarshallWithInitialNamespace() throws WebEngineException, IOException {
        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><UserWithPackageInfoxmlns:ns2=\"http://www.axa.fr/WebEngine/2022\"id=\"1\"><ns2:firstName>Ramesh</ns2:firstName><ns2:lastName>Fadatare</ns2:lastName><ns2:age>25</ns2:age><ns2:gender>Male</ns2:gender></UserWithPackageInfo>";
        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-with-namespace.xml");
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        assertContentXml(fileContentExpected, fileResult);
    }

    @Test
    public void testMarshallWithCustomNamespace() throws WebEngineException, IOException {
        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><nsc:UserWithPackageInfoxmlns:nsc=\"http://www.axa.fr/WebEngine/2022\"id=\"1\"><nsc:firstName>Ramesh</nsc:firstName><nsc:lastName>Fadatare</nsc:lastName><nsc:age>25</nsc:age><nsc:gender>Male</nsc:gender></nsc:UserWithPackageInfo>";
        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-with-namespace.xml");
        String namespace = "http://www.axa.fr/WebEngine/2022";
        Map<String, String> namespaceAndPrefixMap  = new HashMap() {{put(namespace, "nsc");}};
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).namespaceRoot(namespace).namespacePrefixMapper(new NamespacePrefixerWebengine(namespaceAndPrefixMap)).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        assertContentXml(fileContentExpected, fileResult);
    }

    @Test
    public void testMarshallWithoutNamespace() throws WebEngineException, IOException {
        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><UserWithPackageInfoxmlns=\"http://www.axa.fr/WebEngine/2022\"id=\"1\"><firstName>Ramesh</firstName><lastName>Fadatare</lastName><age>25</age><gender>Male</gender></UserWithPackageInfo>";
        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-with-namespace.xml");
        Map<String, String> namespaceAndPrefixMap  = new HashMap() {{put("", "");}};
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).namespaceRoot("http://www.axa.fr/WebEngine/2022").namespacePrefixMapper(new NamespacePrefixerWebengine(namespaceAndPrefixMap)).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        assertContentXml(fileContentExpected, fileResult);
    }

    private void assertContentXml(String fileContentExpected, File fileResult) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(fileResult.getAbsolutePath()));
        String fileResultContent = new String (bytes);
        Assertions.assertEquals(StringUtils.deleteWhitespace(StringUtils.normalizeSpace(fileContentExpected)), StringUtils.deleteWhitespace(StringUtils.normalizeSpace(fileResultContent)));
    }
}