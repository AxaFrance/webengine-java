package fr.axa.automation.webengine.logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoggerServiceProviderTest {

    @Test
    void getInstance() {
        Assertions.assertTrue(LoggerServiceProvider.getInstance() instanceof  LoggerService);
    }
}