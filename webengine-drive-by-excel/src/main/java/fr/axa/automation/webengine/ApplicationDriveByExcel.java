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
public class ApplicationDriveByExcel implements CommandLineRunner {

    final ILoggerService loggerService;

    final IBootProject bootProject;

    @Autowired
    public ApplicationDriveByExcel(LoggerService loggerService, @Qualifier("bootProjectDriveByExcel") IBootProject bootProject) {
        this.loggerService = loggerService;
        this.bootProject = bootProject;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApplicationDriveByExcel.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        loggerService.info("Temporary directory : "+System.getProperty("java.io.tmpdir"));
        //    java -jar drive-by-excel.jar "-data:file.xls"
        //    java -jar drive-by-excel.jar -Dspring.profiles.active=properties-chrome "-data:file.xls" "-tc:firsttestcase[-dataNameColum:jdd-rec-auto;jdd-rec-moto] ; testcase2[-dataNameColum:jdd-rec-moto]" "-browser:Chrome" "-platform:Android"
        //    java -jar drive-by-excel.jar "-data:file.xls" "-tc:firsttestcase, testcase2[-dataNameColum:jdd-rec-moto]"
        //    java -jar drive-by-excel.jar "-data:file.xls" "-tc:firsttestcase, testcase2[-indexColumn:5]"
        //    java -jar drive-by-excel.jar "-data:file.xls" "-tc:firsttestcase, testcase2[-letterColumn:F]"

        bootProject.runFromFramework(args);
    }
}
