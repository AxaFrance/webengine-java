package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestStepDriveByExcelExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Slf4j
@Component
@Qualifier("testStepDriveByExcelExecutor")
public class TestStepDriveByExcelExecutor extends AbstractTestStepExecutor implements ITestStepDriveByExcelExecutor {

    public TestStepDriveByExcelExecutor() {
        super();
    }

    @Override
    public ActionReport run(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws WebEngineException {
//        If optional and contains children , run here
        return null;
    }



    public void executeCmd(){
//        Get cmd from factory
//        Run cmd in another thread
//
    }


}
