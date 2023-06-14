package fr.axa.automation.webengine.core;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Slf4j
public abstract class AbstractTestStepExecutor implements ITestStepExecutor {

    public AbstractTestStepExecutor() {
    }

}
