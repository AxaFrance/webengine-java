package fr.axa.automation.webengine.core;

public interface ITestStep {
    Class<? extends IAction> getAction();
}
