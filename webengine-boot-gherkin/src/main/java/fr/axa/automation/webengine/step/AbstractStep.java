package fr.axa.automation.webengine.step;

import fr.axa.automation.webengine.report.ReportHelperGherkin;

public abstract class AbstractStep {

    protected void addInformation(String information){
        ReportHelperGherkin reportHelperGherkin = ReportHelperGherkin.getInstance();
        reportHelperGherkin.getInformation().add(information);
    }
}
