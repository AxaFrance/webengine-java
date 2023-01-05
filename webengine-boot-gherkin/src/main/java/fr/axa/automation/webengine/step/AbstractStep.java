package fr.axa.automation.webengine.step;

import fr.axa.automation.webengine.context.ExecutionDetail;
import fr.axa.automation.webengine.context.SharedInformation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractStep {

    protected void addInformation(String information){
        String key = ExecutionDetail.STEP_IN_PROGRESS.stream().reduce((one,two) -> two).get();
        SharedInformation.addInformation(key,information);
    }

}
