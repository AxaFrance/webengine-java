package fr.axa.automation.webengine;

import fr.axa.automation.webengine.boot.IBootProject;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootApplication
public class ApplicationNoCode implements CommandLineRunner {

    final ILoggerService loggerService;

    final IBootProject bootProject;

    @Autowired
    public ApplicationNoCode(LoggerService loggerService, @Qualifier("bootProjectNoCode") IBootProject bootProject) {
        this.loggerService = loggerService;
        this.bootProject = bootProject;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApplicationNoCode.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        loggerService.info("***************Start automation***************");
        loggerService.info("Temporary directory : "+System.getProperty("java.io.tmpdir"));
        bootProject.runFromFramework(args);
        loggerService.info("***************End automation***************");
    }
}
