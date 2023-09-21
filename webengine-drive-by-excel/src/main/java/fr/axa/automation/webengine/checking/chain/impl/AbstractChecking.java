package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.constante.OptionalConstante;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.TestCaseDataNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
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

    protected List<CommandDataNoCode> getOptionalCommand(TestCaseDataNoCode testCaseData) {
        return getCommandByOptional(testCaseData,OptionalConstante.OPTIONAL);
    }

    protected List<CommandDataNoCode> getOptionalAndDependsOnPreviousCommand(TestCaseDataNoCode testCaseData) {
        return getCommandByOptional(testCaseData,OptionalConstante.OPTIONAL_AND_DEPENDS_ON_PREVIOUS);
    }

    private List<CommandDataNoCode> getCommandByOptional(TestCaseDataNoCode testCaseData, OptionalConstante optionalConstante) {
        List<CommandDataNoCode> commandDataSet = testCaseData.getCommandList();
        return commandDataSet.stream().filter(commandData -> StringUtil.equalsIgnoreCase(optionalConstante.getValue(),commandData.getOptional())).collect(Collectors.toList());
    }

    public abstract boolean check(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData);

    protected boolean checkNext(AbstractGlobalApplicationContext globalApplicationContext,TestSuiteDataNoCode testSuiteData) {
        if (next == null) {
            return true;
        }
        return next.check(globalApplicationContext,testSuiteData);
    }

    protected List<String> getTestCaseNameList(List<TestCaseDataNoCode> testCaseDataList) {
        return testCaseDataList.stream().map(testCaseData -> testCaseData.getName()).collect(Collectors.toList());
    }
}
