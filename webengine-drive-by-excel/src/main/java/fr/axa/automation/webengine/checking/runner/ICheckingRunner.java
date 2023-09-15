package fr.axa.automation.webengine.checking.runner;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;

public interface ICheckingRunner {

    IChecking getChecking();

    void setChecking(IChecking checking);

    boolean runChecking(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData);
}
