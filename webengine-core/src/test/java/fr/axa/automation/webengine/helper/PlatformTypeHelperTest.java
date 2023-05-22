package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlatformTypeHelperTest {

    @Test
    void testGetPlatformWithEnumValue() throws WebEngineException {
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform("windows"));
    }

    @Test
    void testGetPlatformWithEnumName() throws WebEngineException {
        Assertions.assertEquals(Platform.WINDOWS,PlatformTypeHelper.getPlatform("WINDOWS"));
    }

    @Test
    void testGetPlatformWithWrongValue() throws WebEngineException {
        WebEngineException thrown = Assertions.assertThrows(WebEngineException.class, () -> {
            PlatformTypeHelper.getPlatform("win");
        });
        Assertions.assertEquals("unrecognized Platform value. Possible values are : [Windows, Android, iOS]",thrown.getMessage());
    }
}