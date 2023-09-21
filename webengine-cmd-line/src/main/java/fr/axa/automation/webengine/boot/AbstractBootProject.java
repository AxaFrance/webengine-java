package fr.axa.automation.webengine.boot;

import fr.axa.automation.webengine.argument.ArgumentOption;
import fr.axa.automation.webengine.constante.IConstant;
import fr.axa.automation.webengine.core.ITestSuiteExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.parser.ArgumentParser;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.report.helper.global.IReportHelper;
import fr.axa.automation.webengine.util.JarUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
public abstract class AbstractBootProject implements IBootProject{
    final ILoggerService loggerService;
    final ITestSuiteExecutor testSuiteExecutor;
    final IReportHelper reportHelper;
    final GlobalConfiguration globalConfiguration;

    public AbstractBootProject(ITestSuiteExecutor testSuiteExecutor, IReportHelper reportHelper, ILoggerService loggerService, GlobalConfiguration globalConfiguration) {
        this.loggerService = loggerService;
        this.testSuiteExecutor = testSuiteExecutor;
        this.reportHelper = reportHelper;
        this.globalConfiguration = globalConfiguration;
    }

    public void runFromFramework(String... args) throws Exception {
        loggerService.info("Arguments : "+ Arrays.asList(args));
        CommandLine commandLine = getCommandLine(getArgumentOptionFramework(), args);
        loadProject(commandLine);
        runTestSuite(commandLine);
    }

    public void runFromProject(String... args) throws Exception {
        loggerService.info("Arguments : "+ Arrays.asList(args));
        CommandLine commandLine = getCommandLine(getArgumentOptionProjet(), args);
        runTestSuite(commandLine);
    }

    protected void loadProject(CommandLine commandLine) throws WebEngineException {
        String projectPath = commandLine.getOptionValue(ArgumentOption.PROJECT.getOption());
        loggerService.info("Loading project : " + projectPath + " is running");
        JarUtil.loadLibrary(new File(projectPath));
        loggerService.info("Loading project : " + projectPath + " is succeed");
    }

    protected CommandLine getCommandLine(List<ArgumentOption> argumentOptionList, String[] args) throws ParseException {
        List<String> argumentListForProject = getArgumentsForProject(argumentOptionList,args);
        String[] argumentsListSeparatedByOptionAndValue = getArgumentsSeparatedByOptionAndValue(argumentListForProject);
        return ArgumentParser.getOption(argumentsListSeparatedByOptionAndValue, ArgumentParser.getOptionList(argumentOptionList));
    }

    protected List<String> getArgumentsForProject(List<ArgumentOption> argumentOptionList,String[] args) {
        //--We want to get only arguments for the project, arguments for spring boot not needed
        List<String> filterArguments = Arrays.stream(args).filter(arg ->  ArgumentParser.isOptionForProject(argumentOptionList,arg)).collect(Collectors.toList());
        loggerService.info("Arguments after filter : "+ArgumentParser.removeOptionFromArguments(filterArguments.toArray(new String[0]),ArgumentOption.KEEPASS_PASSWORD));
        return filterArguments;
    }

    protected String[] getArgumentsSeparatedByOptionAndValue(List<String> filterArguments) {
        String[] argumentsForProject = ArgumentParser.splitArguments(filterArguments, IConstant.SEPARATOR_ARG, 2);
        //loggerService.info("Arguments after decomposition : "+ArgumentParser.removePassWordAfterDecompositionArgs(argumentsForProject));
        return argumentsForProject;
    }

    protected abstract void runTestSuite(CommandLine commandLine) throws WebEngineException, IOException;

    protected abstract List<ArgumentOption> getArgumentOptionFramework();

    protected abstract List<ArgumentOption> getArgumentOptionProjet();
}
