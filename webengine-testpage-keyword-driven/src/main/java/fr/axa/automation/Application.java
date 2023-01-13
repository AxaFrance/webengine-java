package fr.axa.automation;

import fr.axa.automation.webengine.boot.BootProject;
import fr.axa.automation.webengine.logger.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    LoggerService loggerService;

    @Autowired
    BootProject bootProject;

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        loggerService.info("Temporary directory application: "+System.getProperty("java.io.tmpdir"));
        bootProject.runFromProject(args);
    }
}
