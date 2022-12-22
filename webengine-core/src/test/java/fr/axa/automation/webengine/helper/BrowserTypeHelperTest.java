package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrowserTypeHelperTest {

    @Test
    void testGetBrowserWithRightValue() throws WebEngineException {
        Assertions.assertEquals(Browser.CHROME,BrowserTypeHelper.getBrowser("Chrome"));
    }

    @Test
    void testGetBrowserWithWrongValue() throws WebEngineException {
        WebEngineException thrown = Assertions.assertThrows(WebEngineException.class, () -> {
            BrowserTypeHelper.getBrowser("Chro");
        });
        Assertions.assertEquals("unrecognized Browser value. Possible values are : [InternetExplorer, Firefox, Chrome, ChromiumEdge, IOSNative, AndroidNative, Safari]",thrown.getMessage());
    }
}