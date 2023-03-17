package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.object.AbstractTestSuiteDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.TestCaseDataDriveByExcel;

import java.util.List;
import java.util.Set;
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

    protected Set<CommandDataDriveByExcel> getCommandDataByName(TestCaseDataDriveByExcel testCaseData, CommandName commandName) {
        Set<CommandDataDriveByExcel> commandDataSet = testCaseData.getCommandList();
        return commandDataSet.stream().filter(commandData -> commandData.getCommand().equalsIgnoreCase(commandName.getName())).collect(Collectors.toSet());
    }

    public abstract boolean check(AbstractTestSuiteDataDriveByExcel testSuiteData);

    protected boolean checkNext(AbstractTestSuiteDataDriveByExcel testSuiteData) {
        if (next == null) {
            return true;
        }
        return next.check(testSuiteData);
    }

    protected List<String> getTestCaseNameList(List<TestCaseDataDriveByExcel> testCaseDataList) {
        return testCaseDataList.stream().map(testCaseData -> testCaseData.getName()).collect(Collectors.toList());
    }
}
