package fr.axa.automation.webengine.core;

import org.junit.jupiter.api.BeforeAll;

class BeforeAndAfterUnitTest {

    
    @BeforeAll
    public static void init() {
        System.out.println("Before");
//        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }


}