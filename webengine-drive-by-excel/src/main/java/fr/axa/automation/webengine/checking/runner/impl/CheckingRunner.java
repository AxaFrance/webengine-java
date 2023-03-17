package fr.axa.automation.webengine.checking.runner.impl;

import fr.axa.automation.webengine.checking.chain.IChecking;
import fr.axa.automation.webengine.checking.runner.ICheckingRunner;
import fr.axa.automation.webengine.object.AbstractTestSuiteDataDriveByExcel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public class CheckingRunner implements ICheckingRunner {

    IChecking checking;

    public boolean runChecking(AbstractTestSuiteDataDriveByExcel testSuiteData) {
        if (checking.check(testSuiteData)) {
            return true;
        }
        return false;
    }
}
