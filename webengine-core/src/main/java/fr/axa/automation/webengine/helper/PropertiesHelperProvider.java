package fr.axa.automation.webengine.helper;

public class PropertiesHelperProvider {

    private static class PropertiesHelperProviderHolder {
        private final static PropertiesHelper INSTANCE = new PropertiesHelper();
    }

    public static PropertiesHelper getInstance(){
        return PropertiesHelperProviderHolder.INSTANCE;
    }
}
