package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BrowserTypeHelperTest {

    @Test
    void testGetBrowserWithEnumValue() throws WebEngineException {
        Assertions.assertEquals(Browser.CHROMIUM_EDGE,BrowserTypeHelper.getBrowser("ChromiumEdge"));
    }

    @Test
    void testGetBrowserWithEnumName() throws WebEngineException {
        Assertions.assertEquals(Browser.CHROMIUM_EDGE,BrowserTypeHelper.getBrowser("CHROMIUM_EDGE"));
    }

    @Test
    void testGetBrowserWithWrongValue() throws WebEngineException {
        WebEngineException thrown = Assertions.assertThrows(WebEngineException.class, () -> {
            BrowserTypeHelper.getBrowser("Chro");
        });
        Assertions.assertEquals("unrecognized Browser value. Possible values are : [InternetExplorer, Firefox, Chrome, ChromiumEdge, IOSNative, AndroidNative, Safari]",thrown.getMessage());
    }
}