package fr.axa.automation.webengine;

import fr.axa.automation.webengine.argument.ArgumentParser;
import fr.axa.automation.webengine.boot.BootProject;
import fr.axa.automation.webengine.constante.IConstant;
import fr.axa.automation.webengine.logger.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        loggerService.info("Temporary directory : "+System.getProperty("java.io.tmpdir"));
        String[] newArgs = ArgumentParser.splitArguments(args, IConstant.SEPARATOR_ARG,2);
        bootProject.runFromFramework(newArgs);
    }
}
