package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.api.ITestSuiteDriveByExcelExecutor;
import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.GlobalApplicationContextDriveByExcel;
import fr.axa.automation.webengine.global.Settings;
import fr.axa.automation.webengine.helper.TestSuiteHelperDriveByExcel;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.AbstractTestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Qualifier("bootProjectDriveByExcel")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProjectDriveByExcel extends AbstractBootProject{

    static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.PROJECT, ArgumentOption.WORKBOOK, ArgumentOption.TEST_CASE_TO_RUN, ArgumentOption.TEST_DATA);

    @Autowired
    public BootProjectDriveByExcel(@Qualifier("testSuiteDriveByExcelExecutor")ITestSuiteExecutor testSuiteExecutor, IReportHelper reportHelper, ILoggerService loggerService, GlobalConfigProperties globalConfigProperties) {
        super(testSuiteExecutor,reportHelper,loggerService,globalConfigProperties);
    }

    @Override
    protected List<ArgumentOption> getArgumentOptionFramework() {
        return ARGUMENT_OPTION_FRAMEWORK;
    }

    @Override
    protected List<ArgumentOption> getArgumentOptionProjet() {
        return null;
    }

    public void runAndLoadExternalProject(List<ArgumentOption> argumentOptionList, String... args) throws Exception {
        super.run(argumentOptionList,args);
    }

    public void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
        AbstractTestSuiteDataDriveByExcel testSuiteData = getTestSuiteData(commandLine);
        AbstractGlobalApplicationContext globalApplicationContext = getGlobalApplicationContext(commandLine);

        loggerService.info("Start Phase initialize test suite ");
        testSuiteExecutor.initialize(globalApplicationContext);
        loggerService.info("End Phase initialize ");

        loggerService.info("Start run test ");

        TestSuiteReport testSuiteReport = ((ITestSuiteDriveByExcelExecutor)testSuiteExecutor).run(globalApplicationContext, testSuiteData);
        loggerService.info("End run test ");

        loggerService.info("Start clean ");
        testSuiteExecutor.cleanUp(globalApplicationContext);
        loggerService.info("End clean ");

        loggerService.info("Start report ");
        reportHelper.generateAllReport(testSuiteReport, "", globalApplicationContext.getSettings().getLogDir());
        loggerService.info("End report ");
    }

    public AbstractGlobalApplicationContext getGlobalApplicationContext(CommandLine commandLine) throws WebEngineException, IOException {
        Settings settings = TestSuiteHelperDriveByExcel.getSettings(commandLine, globalConfigProperties);
        return GlobalApplicationContextDriveByExcel.builder().settings(settings).build();
    }

    protected AbstractTestSuiteDataDriveByExcel getTestSuiteData(CommandLine commandLine){
        return new TestSuiteDataDriveByExcel();
    }
}
