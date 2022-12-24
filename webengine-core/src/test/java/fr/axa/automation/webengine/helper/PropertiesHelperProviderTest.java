package fr.axa.automation.webengine.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesHelperProviderTest {

    @Test
    void getInstance() {
        Assertions.assertTrue(PropertiesHelperProvider.getInstance() instanceof PropertiesHelper);
    }
}