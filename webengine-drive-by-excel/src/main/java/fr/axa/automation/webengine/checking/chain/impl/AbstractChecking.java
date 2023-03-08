package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;
import fr.axa.automation.webengine.object.CommandData;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractChecking implements IChecking {

    private AbstractChecking next;

    public static IChecking link(IChecking first, IChecking... chain) {
        AbstractChecking head = (AbstractChecking) first;
        for (IChecking nextInChain: chain) {
            head.next = (AbstractChecking) nextInChain;
            head = (AbstractChecking) nextInChain;
        }
        return first;
    }

    protected Set<CommandData> getFilterCommandData(Set<CommandData> commandDataSet, CommandName commandName) {
        return commandDataSet.stream().filter(commandData -> commandData.getCommand().equalsIgnoreCase(commandName.getName())).collect(Collectors.toSet());
    }

    public abstract boolean check(AbstractTestSuiteData testSuiteData);

    protected boolean checkNext(AbstractTestSuiteData testSuiteData) {
        if (next == null) {
            return true;
        }
        return next.check(testSuiteData);
    }
}
