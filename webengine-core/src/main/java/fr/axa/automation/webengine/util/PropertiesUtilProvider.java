package fr.axa.automation.webengine.util;

public class PropertiesUtilProvider {

    private static class PropertiesUtilProviderHolder{
        private final static PropertiesUtil INSTANCE = new PropertiesUtil();
    }

    public static PropertiesUtil getInstance(){
        return PropertiesUtilProvider.PropertiesUtilProviderHolder.INSTANCE;
    }
}
