package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.api.ITestSuiteNoCodeExecutor;
import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.constante.ConstantNoCode;
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
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Qualifier("bootProjectDriveByExcel")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class BootProjectNoCode extends AbstractBootProject{
    static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.TEST_CASE_TO_RUN, ArgumentOption.PLATFORM, ArgumentOption.BROWSER, ArgumentOption.OUTPUT_DIR);

    @Autowired
    public BootProjectNoCode(@Qualifier("testSuiteNoCodeExecutor")ITestSuiteExecutor testSuiteExecutor, IReportHelper reportHelper, ILoggerService loggerService, GlobalConfiguration globalConfiguration) {
        super(testSuiteExecutor,reportHelper,loggerService, globalConfiguration);
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
        List<String> optionList = getCommandNameFileOption(args);
        if(CollectionUtils.isNotEmpty(optionList)){
            FileUtil.copyFileFromResource(ConstantNoCode.COMMAND_FILE_NAME.getValue(),optionList.get(1)+ File.separator+ ConstantNoCode.COMMAND_FILE_NAME.getValue());
        }else{
            CommandLine commandLine = getCommandLine(getArgumentOptionFramework(), args);
            runTestSuite(commandLine);
        }
    }

    private List<String> getCommandNameFileOption(String... args){
        if(args!=null && Arrays.asList(args).size()==1 ){
            String[] option = getArgumentsSeparatedByOptionAndValue(Arrays.asList(args));
            if(StringUtil.equalsIgnoreCase(option[0], ConstantNoCode.DASH.getValue() + ArgumentOption.COMMAND_NAME_FILE.getOption())){
                return Arrays.asList(option);
            }
        }
        return new ArrayList<>();
    }

    public void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
        AbstractGlobalApplicationContext globalApplicationContext = getGlobalApplicationContext(commandLine);
        TestSuiteDataNoCode testSuiteData = getTestSuiteData(globalApplicationContext);

        loggerService.info("Start Phase initialize test suite ");
        testSuiteExecutor.initialize(globalApplicationContext);
        loggerService.info("End Phase initialize ");

        loggerService.info("Start run test ");

        TestSuiteReport testSuiteReport = ((ITestSuiteNoCodeExecutor)testSuiteExecutor).run(globalApplicationContext, testSuiteData);
        loggerService.info("End run test ");

        loggerService.info("Start clean ");
        testSuiteExecutor.cleanUp(globalApplicationContext);
        loggerService.info("End clean ");

        loggerService.info("Start report ");
        reportHelper.generateAllReport(testSuiteReport, "", globalApplicationContext.getSettings().getOutputDir());
        loggerService.info("End report ");
    }

    public AbstractGlobalApplicationContext getGlobalApplicationContext(CommandLine commandLine) throws WebEngineException {
        SettingsNoCode settings = TestSuiteHelperNoCode.getSettings(commandLine, globalConfiguration);
        return GlobalApplicationContextNoCode.builder().settings(settings).build();
    }

    protected TestSuiteDataNoCode getTestSuiteData(AbstractGlobalApplicationContext globalApplicationContext){
        SettingsNoCode settingsNoCode = (SettingsNoCode)globalApplicationContext.getSettings();
        return ExcelConverter.convert(settingsNoCode.getDataTestFileName(), settingsNoCode.getTestCaseAndDataTestColumName());
    }
}
