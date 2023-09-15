package fr.axa.automation.webengine.checking.runner.impl;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.checking.runner.ICheckingRunner;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public class CheckingRunner implements ICheckingRunner {

    IChecking checking;

    public boolean runChecking(AbstractGlobalApplicationContext globalApplicationContext,TestSuiteDataNoCode testSuiteData) {
        if (checking.check(globalApplicationContext,testSuiteData)) {
            return true;
        }
        return false;
    }
}
