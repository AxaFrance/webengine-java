package fr.axa.automation.webengine.localtesting;

import com.browserstack.local.Local;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.properties.LocalTesting;
import fr.axa.automation.webengine.util.PropertiesUtilV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
@Component
public class LocalTestingRunner implements ILocalTestingRunner{

    public static final String KEY = "key";
    private Local local ;
    private ILoggerService loggerService;

    @Autowired
    public LocalTestingRunner(ILoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public LocalTestingRunner() {
        this (new LoggerService());
    }

    public void startLocalTesting(String resourceNameOrPathAndFileName) {
        try {
            Optional<GlobalConfigProperties> globalConfigProperties = PropertiesUtilV2.getInstance().getGlobalConfigPropertiesByName(resourceNameOrPathAndFileName);
            if(globalConfigProperties.isPresent() && isLocalTestingActivate(resourceNameOrPathAndFileName)){
                local = new Local();
                local.start(createLocalTestingArguments(globalConfigProperties.get()));
                loggerService.info("Start action - Check if local testing is running : "+local.isRunning());
            }
        }catch (Exception e){
            loggerService.error("Error when start local testing",e);
        }
    }

    private HashMap<String,String> createLocalTestingArguments(GlobalConfigProperties globalConfigProperties ){
        HashMap<String,String> localTestingArguments = new HashMap<>();
        localTestingArguments.put(KEY, globalConfigProperties.getAppiumSettings().getPassword());
        LocalTesting localTesting = globalConfigProperties.getAppiumSettings().getLocalTesting();
        localTesting.getArguments().forEach(localTestingArguments::put);
        loggerService.info("Local testing arguments : "+localTestingArguments.toString());
        return localTestingArguments;
    }

    public void stopLocalTesting() {
        try {
            if(local!=null) {
                local.stop();
                loggerService.info("Stop action - Check if local testing is running : "+local.isRunning());
            }
        }catch (Exception e){
            loggerService.error("Error when stop local testing",e);
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
