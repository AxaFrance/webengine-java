package fr.axa.automation.webengine.localtesting;

public interface ILocalTestingRunner {

    void startLocalTesting();

    void startLocalTesting(String resourceNameOrPathAndFileName);

    void stopLocalTesting() ;
}
