package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.Constante;
import fr.axa.automation.webengine.constante.OptionalConstante;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;
import fr.axa.automation.webengine.object.TestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.util.StringUtil;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractChecking implements IChecking {

    private AbstractChecking next;

    protected static final ILoggerService loggerService = LoggerServiceProvider.getInstance();

    public static IChecking link(IChecking first, IChecking... chain) {
        AbstractChecking head = (AbstractChecking) first;
        for (IChecking nextInChain: chain) {
            head.next = (AbstractChecking) nextInChain;
            head = (AbstractChecking) nextInChain;
        }
        return first;
    }

    protected List<CommandDataDriveByExcel> getCommandDataByName(TestCaseDataDriveByExcel testCaseData, CommandName commandName) {
        List<CommandDataDriveByExcel> commandDataSet = testCaseData.getCommandList();
        return commandDataSet.stream().filter(commandData -> commandData.getCommand() == commandName).collect(Collectors.toList());
    }

    protected List<CommandDataDriveByExcel> getOptionalCommand(TestCaseDataDriveByExcel testCaseData) {
        return getCommandByOptional(testCaseData,OptionalConstante.OPTIONAL);
    }

    protected List<CommandDataDriveByExcel> getOptionalAndDependsOnPreviousCommand(TestCaseDataDriveByExcel testCaseData) {
        return getCommandByOptional(testCaseData,OptionalConstante.OPTIONAL_AND_DEPENDS_ON_PREVIOUS);
    }

    private List<CommandDataDriveByExcel> getCommandByOptional(TestCaseDataDriveByExcel testCaseData, OptionalConstante optionalConstante) {
        List<CommandDataDriveByExcel> commandDataSet = testCaseData.getCommandList();
        return commandDataSet.stream().filter(commandData -> StringUtil.equalsIgnoreCase(optionalConstante.getValue(),commandData.getOptional())).collect(Collectors.toList());
    }

    public abstract boolean check(TestSuiteDataDriveByExcel testSuiteData);

    protected boolean checkNext(TestSuiteDataDriveByExcel testSuiteData) {
        if (next == null) {
            return true;
        }
        return next.check(testSuiteData);
    }

    protected List<String> getTestCaseNameList(List<TestCaseDataDriveByExcel> testCaseDataList) {
        return testCaseDataList.stream().map(testCaseData -> testCaseData.getName()).collect(Collectors.toList());
    }
}
