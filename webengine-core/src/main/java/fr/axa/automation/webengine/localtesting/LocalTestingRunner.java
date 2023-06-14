package fr.axa.automation.webengine.localtesting;

import com.browserstack.local.Local;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.helper.PropertiesHelperProvider;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfiguration;
import fr.axa.automation.webengine.properties.LocalTestingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
@Component
public class LocalTestingRunner implements ILocalTestingRunner{

    public static final String KEY = "key";

    private Local local ;

    private final ILoggerService loggerService;

    private final GlobalConfiguration globalConfiguration;

    @Autowired
    public LocalTestingRunner(ILoggerService loggerService, GlobalConfiguration globalConfiguration) {
        this.loggerService = loggerService;
        this.globalConfiguration = globalConfiguration;
    }

    public LocalTestingRunner() {
        this (new LoggerService(),null);
    }

    public void startLocalTesting() {
        if(globalConfiguration !=null && isLocalTestingActivate(Optional.of(globalConfiguration))) {
            startLocalTesting(globalConfiguration);
        }
    }

    public void startLocalTesting(String resourceNameOrPathAndFileName) {
        Optional<GlobalConfiguration> globalConfigProperties = getGlobalConfigProperties(resourceNameOrPathAndFileName);
        if(globalConfigProperties.isPresent() && isLocalTestingActivate(globalConfigProperties)) {
            startLocalTesting(globalConfigProperties.get());
        }
    }

    private void startLocalTesting(GlobalConfiguration globalConfiguration) {
        try {
            local = new Local();
            local.start(getLocalTestingArguments(globalConfiguration));
            loggerService.info("Start action - Check if local testing is running : " + local.isRunning());
        } catch (Exception e) {
            loggerService.error("Error when start local testing", e);
        }
    }

    private Optional<GlobalConfiguration> getGlobalConfigProperties(String resourceNameOrPathAndFileName) {
        try {
            return PropertiesHelperProvider.getInstance().getGlobalConfigurationByName(resourceNameOrPathAndFileName);
        }catch(WebEngineException e){
            loggerService.error("No File "+resourceNameOrPathAndFileName+" found or error during loading file",e);
        }
        return Optional.empty();
    }

    private HashMap<String,String> getLocalTestingArguments(GlobalConfiguration globalConfiguration){
        HashMap<String,String> localTestingArguments = new HashMap<>();
        localTestingArguments.put(KEY, globalConfiguration.getWebengineConfiguration().getAppiumConfiguration().getPassword());
        localTestingArguments.putAll(globalConfiguration.getWebengineConfiguration().getAppiumConfiguration().getLocalTesting().getArguments());
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

    private boolean isLocalTestingActivate(Optional<GlobalConfiguration> globalConfigProperties) {
        if(globalConfigProperties.isPresent() && globalConfigProperties.get().isLocalTestingConfExist()) {
            LocalTestingConfiguration localTestingConfiguration = globalConfigProperties.get().getWebengineConfiguration().getAppiumConfiguration().getLocalTesting();
            if (localTestingConfiguration.isActivate()) {
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