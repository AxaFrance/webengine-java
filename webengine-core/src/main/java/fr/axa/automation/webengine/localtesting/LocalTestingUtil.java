package fr.axa.automation.webengine.localtesting;

import com.browserstack.local.Local;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.properties.LocalTesting;
import fr.axa.automation.webengine.util.PropertiesUtilV2;

import java.util.HashMap;
import java.util.Optional;

public class LocalTestingUtil {


    public static final String KEY = "key";
    private Local local ;
    private LoggerService loggerService;

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

    public void startLocalTesting(String resourceNameOrPathAndFileName) throws Exception {
        Optional<GlobalConfigProperties> globalConfigProperties = PropertiesUtilV2.getInstance().getGlobalConfigPropertiesByName(resourceNameOrPathAndFileName);
        if(isLocalTestingActivate(resourceNameOrPathAndFileName)){
            local = new Local();
            local.start(createLocalTestingArguments(globalConfigProperties.get()));
            loggerService.info("Start action - Check if local testing is running : "+local.isRunning());
        }
    }

    public void stopLocalTesting() throws Exception {
        if(local!=null) {
            local.stop();
            loggerService.info("Stop action - Check if local testing is running : "+local.isRunning());
        }
    }

    private boolean isLocalTestingActivate(String resourceNameOrPathAndFileName) throws Exception {
        boolean activate = false;
        Optional<GlobalConfigProperties> globalConfigProperties = PropertiesUtilV2.getInstance().getGlobalConfigPropertiesByName(resourceNameOrPathAndFileName);
        if(isLocalTestingConfExist(globalConfigProperties)) {
            LocalTesting localTesting = globalConfigProperties.get().getAppiumSettings().getLocalTesting();
            if (localTesting.isActivate()) {
                activate = true;
            }else{
                loggerService.info("Local testing flag is not activate if you run your testing app in mobile");
            }
        }else{
            loggerService.info("Local testing isn't configured if you run your testing app in mobile");
        }
        return activate;
    }

    private boolean isLocalTestingConfExist(Optional<GlobalConfigProperties> globalConfigProperties) {
        if (globalConfigProperties.isPresent() &&
                globalConfigProperties.get().getAppiumSettings()!=null &&
                globalConfigProperties.get().getAppiumSettings().getLocalTesting()!=null){
          return true;
        }
        return false;
    }
}
