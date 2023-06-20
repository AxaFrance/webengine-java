package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.api.ITestSuiteNoCodeExecutor;
import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.GlobalApplicationContextNoCode;
import fr.axa.automation.webengine.global.SettingsNoCode;
import fr.axa.automation.webengine.helper.ExcelConverter;
import fr.axa.automation.webengine.helper.TestSuiteHelperNoCode;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.constante.ReportPathKey;
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("bootProjectNoCode")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProjectNoCode extends AbstractBootProject {
    static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.TEST_CASE_TO_RUN, ArgumentOption.PLATFORM, ArgumentOption.BROWSER, ArgumentOption.OUTPUT_DIR, ArgumentOption.SHOW_REPORT);

    @Autowired
    public BootProjectNoCode(@Qualifier("testSuiteNoCodeExecutor") ITestSuiteExecutor testSuiteExecutor, IReportHelper reportHelper, ILoggerService loggerService, GlobalConfiguration globalConfiguration) {
        super(testSuiteExecutor, reportHelper, loggerService, globalConfiguration);
    }

    @Override
    protected List<ArgumentOption> getArgumentOptionFramework() {
        return ARGUMENT_OPTION_FRAMEWORK;
    }

    @Override
    protected List<ArgumentOption> getArgumentOptionProjet() {
        return null;
    }

    public void runFromFramework(String... args) throws Exception {
        CommandLine commandLine = getCommandLine(getArgumentOptionFramework(), args);
        runTestSuite(commandLine);
    }

    public void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
        AbstractGlobalApplicationContext globalApplicationContext = getGlobalApplicationContext(commandLine);
        TestSuiteDataNoCode testSuiteData = getTestSuiteData(globalApplicationContext);

        loggerService.info("Start Phase initialize test suite ");
        testSuiteExecutor.initialize(globalApplicationContext);
        loggerService.info("End Phase initialize ");

        loggerService.info("Start run test ");

        TestSuiteReport testSuiteReport = ((ITestSuiteNoCodeExecutor) testSuiteExecutor).run(globalApplicationContext, testSuiteData);
        loggerService.info("End run test ");

        loggerService.info("Start clean ");
        testSuiteExecutor.cleanUp(globalApplicationContext);
        loggerService.info("End clean ");

        loggerService.info("Start report ");
        Map<ReportPathKey,String> reportsPath = reportHelper.generateReports(testSuiteReport, "", globalApplicationContext.getSettings().getOutputDir());
        loggerService.info("End report ");

        if(globalApplicationContext.getSettings().isShowReport()) {
            loggerService.info("Open report ");
            reportHelper.openReport(reportsPath.get(ReportPathKey.HTML_REPORT_PATH_KEY) + File.separator + "index.html");
            loggerService.info("End open report ");
        }
    }

    public AbstractGlobalApplicationContext getGlobalApplicationContext(CommandLine commandLine) throws WebEngineException {
        SettingsNoCode settings = TestSuiteHelperNoCode.getSettings(commandLine, globalConfiguration);
        return GlobalApplicationContextNoCode.builder().settings(settings).build();
    }

    protected TestSuiteDataNoCode getTestSuiteData(AbstractGlobalApplicationContext globalApplicationContext) {
        SettingsNoCode settingsNoCode = (SettingsNoCode) globalApplicationContext.getSettings();
        return ExcelConverter.convert(settingsNoCode.getDataTestFileName(), settingsNoCode.getTestCaseAndDataTestColumName());
    }
}