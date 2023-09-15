package fr.axa.automation.webengine.checking.chain;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;

public interface IChecking {
    boolean check(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData);
}
