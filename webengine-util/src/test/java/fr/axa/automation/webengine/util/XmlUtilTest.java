package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.jaxb.withoutpackageinfo.UserWithoutPackageInfo;
import fr.axa.automation.webengine.jaxb.withpackageinfo.UserWithPackageInfo;
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
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).namespace(namespace).prefix("nsc").build();
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
        InputMarshallDTO inputMarshallDTO = InputMarshallDTO.builder().fileDestinationPath(filePath).objectToMarshall(user).namespace("http://www.axa.fr/WebEngine/2022").prefix("").build();
        File fileResult = XmlUtil.marshall(inputMarshallDTO);
        boolean resultCompareFile = FileUtil.assertContent(FileUtil.getFileFromResource("xml/user-without-namespace.xml"), fileResult);
        Assertions.assertTrue(resultCompareFile);
    }
}

