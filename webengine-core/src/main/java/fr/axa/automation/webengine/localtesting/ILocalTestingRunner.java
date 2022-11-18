package fr.axa.automation.webengine.localtesting;

public interface ILocalTestingRunner {

    void startLocalTesting(String resourceNameOrPathAndFileName);

    void stopLocalTesting() ;
}
