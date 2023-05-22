package fr.axa.automation.webengine.localtesting;

public class LocalTestingProvider {

    private static class LocalTestingRunnerHolder {
        private static final LocalTestingRunner INSTANCE = new LocalTestingRunner();
    }
    public static LocalTestingRunner getInstance(){
        return LocalTestingRunnerHolder.INSTANCE;
    }
}
