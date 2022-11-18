package fr.axa.automation.webengine.step;

import fr.axa.automation.webengine.report.ReportGherkinHelper;

public abstract class AbstractStep {

    protected void addInformation(String information){
        ReportGherkinHelper reportGherkinHelper = ReportGherkinHelper.getInstance();
        reportGherkinHelper.getInformation().add(information);
    }
}
