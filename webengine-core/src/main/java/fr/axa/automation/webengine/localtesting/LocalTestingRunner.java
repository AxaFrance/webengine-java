package fr.axa.automation.webengine.localtesting;

import com.browserstack.local.Local;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.helper.PropertiesHelperProvider;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.properties.LocalTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
@Component
public class LocalTestingRunner implements ILocalTestingRunner{

    public static final String KEY = "key";

    private Local local ;

    private final ILoggerService loggerService;

    private final GlobalConfigProperties globalConfigProperties;

    @Autowired
    public LocalTestingRunner(ILoggerService loggerService, GlobalConfigProperties globalConfigProperties) {
        this.loggerService = loggerService;
        this.globalConfigProperties = globalConfigProperties;
    }

    public LocalTestingRunner() {
        this (new LoggerService(),null);
    }

    public void startLocalTesting() {
        if(globalConfigProperties!=null && isLocalTestingActivate(Optional.of(globalConfigProperties))) {
            startLocalTesting(globalConfigProperties);
        }
    }

    public void startLocalTesting(String resourceNameOrPathAndFileName) {
        Optional<GlobalConfigProperties> globalConfigProperties = getGlobalConfigProperties(resourceNameOrPathAndFileName);
        if(globalConfigProperties.isPresent() && isLocalTestingActivate(globalConfigProperties)) {
            startLocalTesting(globalConfigProperties.get());
        }
    }

    private void startLocalTesting(GlobalConfigProperties globalConfigProperties) {
        try {
            local = new Local();
            local.start(getLocalTestingArguments(globalConfigProperties));
            loggerService.info("Start action - Check if local testing is running : " + local.isRunning());
        } catch (Exception e) {
            loggerService.error("Error when start local testing", e);
        }
    }

    private Optional<GlobalConfigProperties> getGlobalConfigProperties(String resourceNameOrPathAndFileName) {
        try {
            return PropertiesHelperProvider.getInstance().getGlobalConfigurationByName(resourceNameOrPathAndFileName);
        }catch(WebEngineException e){
            loggerService.error("No File "+resourceNameOrPathAndFileName+" found or error during loading file",e);
        }
        return Optional.empty();
    }

    private HashMap<String,String> getLocalTestingArguments(GlobalConfigProperties globalConfigProperties ){
        HashMap<String,String> localTestingArguments = new HashMap<>();
        localTestingArguments.put(KEY, globalConfigProperties.getAppiumSettings().getPassword());
        localTestingArguments.putAll(globalConfigProperties.getAppiumSettings().getLocalTesting().getArguments());
        loggerService.info("Local testing arguments : "+localTestingArguments);
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

    private boolean isLocalTestingActivate(Optional<GlobalConfigProperties> globalConfigProperties) {
        if(globalConfigProperties.isPresent() && globalConfigProperties.get().isLocalTestingConfExist()) {
            LocalTesting localTesting = globalConfigProperties.get().getAppiumSettings().getLocalTesting();
            if (localTesting.isActivate()) {
                return true;
            }else{
                loggerService.info("Local testing flag is not activate if you run your testing app in mobile");
            }
        }else{
            loggerService.info("Local testing isn't configured if you run your testing app in mobile");
        }
        return false;
    }
}