package fr.axa.automation.webengine.checking.runner;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.object.AbstractTestSuiteData;

public interface ICheckingRunner {

    IChecking getChecking();

    void setChecking(IChecking checking);

    boolean runChecking(AbstractTestSuiteData testSuiteData);
}
