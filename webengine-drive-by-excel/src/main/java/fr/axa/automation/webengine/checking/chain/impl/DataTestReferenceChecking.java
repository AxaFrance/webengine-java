package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.object.AbstractTestSuiteData;

public class DataTestReferenceChecking extends AbstractChecking{
    @Override
    public boolean check(AbstractTestSuiteData testSuiteData) {
        return checkNext(testSuiteData);
    }
}
