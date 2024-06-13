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
import com.microsoft.applicationinsights.attach.ApplicationInsights;

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
        if (System.getProperty("applicationinsights.runtime-attach.configuration.classpath.file") == null) {
            System.setProperty("applicationinsights.role.name",System.getenv("USERNAME"));
            System.setProperty("applicationinsights.runtime-attach.configuration.classpath.file", "applicationinsights-dev.json");
            ApplicationInsights.attach();
        }
        new SpringApplicationBuilder(ApplicationNoCode.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        // "-tc:firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto];testcase2[-dataColumName:jdd-rec-moto]"
        loggerService.info("***************Start automation***************");
        loggerService.info("Temporary directory : "+System.getProperty("java.io.tmpdir"));
        bootProject.runFromFramework(args);
        loggerService.info("***************End automation***************");
    }
}
