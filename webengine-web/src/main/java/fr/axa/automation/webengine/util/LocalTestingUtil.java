package fr.axa.automation.webengine.util;

import com.browserstack.local.Local;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.properties.LocalTesting;

import java.util.HashMap;
import java.util.Optional;

public class LocalTestingUtil {

    public static final String APPLICATION_FILE_NAME = "application-properties.yml";
    public static final String KEY = "key";
    Local local ;
    LoggerService loggerService;

    public LocalTestingUtil() {
        this.loggerService = new LoggerService();
    }

    private static class LocalTestingUtilHolder{
        private final static LocalTestingUtil INSTANCE = new LocalTestingUtil();
    }

    public static LocalTestingUtil getInstance(){
        return LocalTestingUtil.LocalTestingUtilHolder.INSTANCE;
    }

    private HashMap<String,String> createLocalTestingArguments(GlobalConfigProperties globalConfigProperties ){
        HashMap<String,String> localTestingArguments = new HashMap<>();
        localTestingArguments.put(KEY, globalConfigProperties.getAppiumSettings().getPassword());
        LocalTesting localTesting = globalConfigProperties.getAppiumSettings().getLocalTesting();
        localTesting.getArguments().forEach((key, value) -> localTestingArguments.put(key, value));
        return localTestingArguments;
    }

    public void startLocalTesting(String propertyFileName) throws Exception {
        Optional<GlobalConfigProperties> globalConfigProperties = PropertiesUtilV2.getInstance().getGlobalConfigPropertiesByName(propertyFileName);
        if(isLocalTestingPresent(globalConfigProperties)){
            LocalTesting localTesting = globalConfigProperties.get().getAppiumSettings().getLocalTesting();
            if(localTesting.isActivate()){
                local = new Local();
                local.start(createLocalTestingArguments(globalConfigProperties.get()));
                loggerService.info("Check if local testing is running : "+local.isRunning());
            }else{
                loggerService.info("Local testing is not activate if you run your testing app in mobile");
            }
        }else{
            loggerService.info("Local testing isn't configured if you run your testing app in mobile");
        }
    }

    public void stopLocalTesting() throws Exception {
        local.stop();
    }

    private boolean isLocalTestingPresent(Optional<GlobalConfigProperties> globalConfigProperties) {
        if (globalConfigProperties.isPresent() &&
                globalConfigProperties.get().getAppiumSettings()!=null &&
                globalConfigProperties.get().getAppiumSettings().getLocalTesting()!=null){
          return true;
        }
        return false;
    }
}
