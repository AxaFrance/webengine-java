package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.api.ITestSuiteDriveByExcelExecutor;
import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.GlobalApplicationContextDriveByExcel;
import fr.axa.automation.webengine.global.SettingsDriveByExcel;
import fr.axa.automation.webengine.helper.ExcelConverter;
import fr.axa.automation.webengine.helper.TestSuiteHelperDriveByExcel;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
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
public class BootProjectDriveByExcel extends AbstractBootProject{
    static final List<ArgumentOption> ARGUMENT_OPTION_FRAMEWORK = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.TEST_CASE_TO_RUN, ArgumentOption.PLATFORM, ArgumentOption.BROWSER, ArgumentOption.OUTPUT_DIR);

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

    public void runFromFramework(String... args) throws Exception {
        List<String> optionList = getCommandNameFileOption(args);
        if(CollectionUtils.isNotEmpty(optionList)){
            FileUtil.copyFile(Constante.COMMAND_FILE_NAME.getValue(),optionList.get(1)+ File.separator+Constante.COMMAND_FILE_NAME.getValue());
        }else{
            CommandLine commandLine = getCommandLine(getArgumentOptionFramework(), args);
            runTestSuite(commandLine);
        }
    }

    private List<String> getCommandNameFileOption(String... args){
        if(args!=null && Arrays.asList(args).size()==1 ){
            String[] option = getArgumentsSeparatedByOptionAndValue(Arrays.asList(args));
            if(StringUtil.equalsIgnoreCase(option[0], Constante.DASH.getValue() + ArgumentOption.COMMAND_NAME_FILE.getOption())){
                return Arrays.asList(option);
            }
        }
        return new ArrayList<>();
    }

    public void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException {
        AbstractGlobalApplicationContext globalApplicationContext = getGlobalApplicationContext(commandLine);
        TestSuiteDataDriveByExcel testSuiteData = getTestSuiteData(globalApplicationContext);

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
        SettingsDriveByExcel settings = TestSuiteHelperDriveByExcel.getSettings(commandLine, globalConfigProperties);
        return GlobalApplicationContextDriveByExcel.builder().settings(settings).build();
    }

    protected TestSuiteDataDriveByExcel getTestSuiteData(AbstractGlobalApplicationContext globalApplicationContext){
        SettingsDriveByExcel settingsDriveByExcel = (SettingsDriveByExcel)globalApplicationContext.getSettings();
        return ExcelConverter.convert(settingsDriveByExcel.getFileName(),settingsDriveByExcel.getTestCaseAndDataTestColumName());
    }
}
