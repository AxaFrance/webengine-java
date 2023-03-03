package fr.axa.automation.webengine.api;

public interface ITestStep {
    Class<? extends IAction> getAction();
    default Class<? extends IAction> getMobileAction(){
        return null;
    }
}
