package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.jaxb.withoutpackageinfo.UserWithoutPackageInfo;
import fr.axa.automation.webengine.jaxb.withpackageinfo.UserWithPackageInfo;
import fr.axa.automation.webengine.xml.NamespacePrefixerWebengine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
        Assertions.assertEquals(1, user.getId());
        Assertions.assertEquals("Ramesh", user.getFirstName());
    }

    @Test
    public void testMarshallWithoutNamespaceAndPackageInfo() throws WebEngineException, IOException, URISyntaxException {
        UserWithoutPackageInfo user = UserUtilForTest.getNewUserWithoutPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST, "user-without-namespace-and-package-info.xml");
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        boolean resultCompareFile = FileUtil.assertContent(FileUtil.getFileFromResource("xml/user-without-namespace-and-package-info.xml"), fileResult);
        Assertions.assertTrue(resultCompareFile);
    }

    @Test
    public void testMarshallWithInitialNamespace() throws WebEngineException, IOException, URISyntaxException {
        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST, "user-with-initial-namespace.xml");
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        boolean resultCompareFile = FileUtil.assertContent(FileUtil.getFileFromResource("xml/user-with-initial-namespace.xml"), fileResult);
        Assertions.assertTrue(resultCompareFile);
    }

    @Test
    public void testMarshallWithCustomNamespace() throws WebEngineException, IOException, URISyntaxException {
        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST, "user-with-custom-namespace.xml");
        String namespace = "http://www.axa.fr/WebEngine/2022";
        Map<String, String> namespaceAndPrefixMap = new HashMap() {{
            put(namespace, "nsc");
        }};
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).namespaceRoot(namespace).namespacePrefixMapper(new NamespacePrefixerWebengine(namespaceAndPrefixMap)).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        boolean resultCompareFile = FileUtil.assertContent(FileUtil.getFileFromResource("xml/user-with-custom-namespace.xml"), fileResult);
        Assertions.assertTrue(resultCompareFile);
    }

    @Test
    public void testMarshallWithoutNamespace() throws WebEngineException, IOException, URISyntaxException {
        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST, "user-without-namespace.xml");
        Map<String, String> namespaceAndPrefixMap = new HashMap() {{
            put("", "");
        }};
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).namespaceRoot("http://www.axa.fr/WebEngine/2022").namespacePrefixMapper(new NamespacePrefixerWebengine(namespaceAndPrefixMap)).build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        boolean resultCompareFile = FileUtil.assertContent(FileUtil.getFileFromResource("xml/user-without-namespace.xml"), fileResult);
        Assertions.assertTrue(resultCompareFile);
    }

//    @Test
//    public void testMarshallWithoutNamespace1() throws WebEngineException, IOException {
//        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><userwithoutpackageinfoid=\"1\"><firstName>Ramesh</firstName><lastName>Fadatare</lastName><age>25</age><gender>Male</gender></userwithoutpackageinfo>";
//        UserWithoutPackageInfo user = UserUtilForTest.getNewUserWithoutPackageInfo();
//        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST, "jaxb-users-without-namespace.xml");
//        File fileResult = XmlUtil.marshallWithoutNamespace(filePath, user);
////        assertContentXml(fileContentExpected, fileResult);
//    }
//
//    @Test
//    public void testMarshallWitNamespace() throws WebEngineException, IOException {
//        String fileContentExpected = "<?xmlversion=\"1.0\"encoding=\"UTF-8\"standalone=\"yes\"?><ns:UserWithPackageInfoxmlns:ns=\"http://www.axa.fr/WebEngine/2022\"id=\"1\"><ns:firstName>Ramesh</ns:firstName><ns:lastName>Fadatare</ns:lastName><ns:age>25</ns:age><ns:gender>Male</ns:gender></ns:UserWithPackageInfo>";
//        UserWithPackageInfo user = UserUtilForTest.getNewUserWithPackageInfo();
//        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST, "jaxb-users-with-namespace.xml");
//        File fileResult = XmlUtil.marshallWithNamespace(filePath, user, "http://www.axa.fr/WebEngine/2022", "ns");
////        assertContentXml(fileContentExpected,fileResult);
//    }
}

