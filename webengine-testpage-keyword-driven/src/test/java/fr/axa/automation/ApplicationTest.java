package fr.axa.automation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(args={"-data:C:\\work\\projet-git\\webengine-testpage-keyword-driven\\src\\test\\resources\\input\\data.xml","-env:C:\\work\\projet-git\\webengine-testpage-keyword-driven\\src\\test\\resources\\input\\env.xml"})
@ActiveProfiles("windows-chromiumedge")
public class ApplicationTest {

    @Test
    public void contextLoadsAndRunMainMethod() {
    }
}