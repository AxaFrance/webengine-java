package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.checking.chain.impl.AbstractChecking;
import fr.axa.automation.webengine.checking.chain.impl.CallScenariiChecking;
import fr.axa.automation.webengine.checking.chain.impl.IfChecking;
import fr.axa.automation.webengine.checking.chain.impl.OptionalChecking;
import fr.axa.automation.webengine.checking.chain.impl.TestCaseArgChecking;
import fr.axa.automation.webengine.checking.runner.ICheckingRunner;
import fr.axa.automation.webengine.checking.runner.impl.CheckingRunner;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.executor.ITestSuiteNoCodeExecutor;
import fr.axa.automation.webengine.executor.TestSuiteNoCodeExecutor;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.GlobalApplicationContextNoCode;
import fr.axa.automation.webengine.global.SettingsNoCode;
import fr.axa.automation.webengine.helper.ExcelConverter;
import fr.axa.automation.webengine.helper.TestSuiteHelperNoCode;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerAppender;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import fr.axa.automation.webengine.parser.ArgumentParser;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.constante.ReportPathKey;
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import fr.axa.automation.webengine.util.ApplicationDesktop;
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
    static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.TEST_CASE_TO_RUN, ArgumentOption.PLATFORM, ArgumentOption.BROWSER, ArgumentOption.OUTPUT_DIR, ArgumentOption.SHOW_REPORT,ArgumentOption.CLOSE_BROWSER_AFTER_EACH_SCENARIO, ArgumentOption.KEEPASS_PASSWORD, ArgumentOption.KEEPASS_FILE, ArgumentOption.DELETE_TEMP_FILE);

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

    @Override
    public void runFromFramework(String... args) throws Exception {
        loggerService.info("Arguments : "+ ArgumentParser.removeOptionFromArguments(args,ArgumentOption.KEEPASS_PASSWORD));
        TestSuiteReport testSuiteReport = null;
        try{
            CommandLine commandLine = getCommandLine(getArgumentOptionFramework(), args);
            AbstractGlobalApplicationContext globalApplicationContext = getGlobalApplicationContext(commandLine);
            TestSuiteDataNoCode testSuiteData = getTestSuiteData(globalApplicationContext);
            checkInput(globalApplicationContext,testSuiteData);

            testSuiteExecutor.initialize(globalApplicationContext);
            testSuiteReport = ((ITestSuiteNoCodeExecutor) testSuiteExecutor).run(globalApplicationContext, testSuiteData);
            testSuiteExecutor.cleanUp(globalApplicationContext);
            Map<ReportPathKey,String> reportsPath = reportHelper.generateReports(testSuiteReport, "", globalApplicationContext.getSettings().getOutputDir());

            if(globalApplicationContext.getSettings().isShowReport()) {
                reportHelper.openReport(reportsPath.get(ReportPathKey.HTML_REPORT_PATH_KEY) + File.separator + "index.html");
            }
            if (((SettingsNoCode)globalApplicationContext.getSettings()).isDeleteTempFile()){
                ((TestSuiteNoCodeExecutor) testSuiteExecutor).deleteTempFile(((SettingsNoCode) globalApplicationContext.getSettings()).getDataTestFileName());
            }
        }catch (Exception e){
            loggerService.error("Error during execution of the automate. You can see more details in the file log",e);
            ApplicationDesktop.openFile(LoggerAppender.getFileAppender());
        }
        if(testSuiteReport!=null && testSuiteReport.getFailed()>0){
            String errorMsg = "Test suite failed. Total number of test case :" + testSuiteReport.getNumberOfTestcase() +". Number of failed test : "+testSuiteReport.getFailed();
            loggerService.error(errorMsg);
            ApplicationDesktop.openFile(LoggerAppender.getFileAppender());
            throw new Exception(errorMsg);
        }
    }

    public void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
    }

    public AbstractGlobalApplicationContext getGlobalApplicationContext(CommandLine commandLine) throws WebEngineException {
        SettingsNoCode settings = TestSuiteHelperNoCode.getSettings(commandLine, globalConfiguration);
        return GlobalApplicationContextNoCode.builder().settings(settings).build();
    }

    protected TestSuiteDataNoCode getTestSuiteData(AbstractGlobalApplicationContext globalApplicationContext) {
        SettingsNoCode settingsNoCode = (SettingsNoCode) globalApplicationContext.getSettings();
        return ExcelConverter.convert(settingsNoCode.getDataTestFileName(), settingsNoCode.getTestCaseAndDataTestColumName());
    }

    public void checkInput(AbstractGlobalApplicationContext globalApplicationContext,TestSuiteDataNoCode testSuiteData) {
        IChecking checking = AbstractChecking.link(
                new IfChecking(),
                new OptionalChecking(),
                new CallScenariiChecking(),
                new TestCaseArgChecking()
        );
        ICheckingRunner checkingRunner = new CheckingRunner();
        checkingRunner.setChecking(checking);
        checkingRunner.runChecking(globalApplicationContext,testSuiteData);
    }
}