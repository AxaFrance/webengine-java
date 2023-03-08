package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.ITestStepDriveByExcelExecutor;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.global.GlobalApplicationContext;
import fr.axa.automation.webengine.object.CommandData;
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
    public ActionReport run(GlobalApplicationContext globalApplicationContext, ITestCaseContext testCaseContext, CommandData commandData) throws WebEngineException {
        return null;
    }



    public void executeCmd(){
//        Get cmd from factory
//        Run cmd in another thread
//
    }


}
