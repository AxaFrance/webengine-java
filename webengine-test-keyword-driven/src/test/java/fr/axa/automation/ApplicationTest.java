package fr.axa.automation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(args={"-data:src\\test\\resources\\input\\data.xml","-env:src\\test\\resources\\input\\env.xml","-tc:TEST_CASE_1"})
@ActiveProfiles("windows-chrome")
public class ApplicationTest {

    @Test
    public void contextLoadsAndRunMainMethod() {
    }
}