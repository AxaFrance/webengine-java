package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.jaxb.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class XmlUtilTest {

    private static User getUserFromXml(String filePath) throws URISyntaxException, WebEngineException {
        return XmlUtil.unmarshall(filePath, User.class);
    }

    @Test
    public void testUnmarshall() throws URISyntaxException, WebEngineException {
        User user = getUserFromXml(FileUtil.getFileFromResource("jaxb-users.xml").getAbsolutePath());
        Assertions.assertEquals(1,user.getId());
    }


    @Test
    public void testMarshallWithoutNamespace() throws URISyntaxException, WebEngineException, IOException {
        User user = UserUtilForTest.getNewUserTest();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-without-namespace.xml");
        XmlUtil.marshallWithoutNamespace(filePath, user);
        Assertions.assertEquals(1,getUserFromXml(filePath).getId());
    }

    @Test
    public void testMarshallWitNamespace() throws URISyntaxException, WebEngineException, IOException {
        User user = UserUtilForTest.getNewUserTest();
        String filePath = FileUtilForTest.createFileInTargetDirectory(FileUtilForTest.DIRECTORY_RESULT_UNIT_TEST,"jaxb-users-with-namespace.xml");
        XmlUtil.marshallWithNamespace(filePath, user,"http://www.axa.fr/WebEngine/2022","ns");
        Assertions.assertEquals(1,getUserFromXml(filePath).getId());
    }
}