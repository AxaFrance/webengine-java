package fr.axa.automation.webengine.step;

import fr.axa.automation.webengine.context.ExecutionDetail;

public abstract class AbstractStep {

    protected void addInformation(String information){
        ExecutionDetail.addInformation(information);
    }
}