package fr.axa.automation.webengine.step;

import fr.axa.automation.webengine.context.ExecutionDetail;
import fr.axa.automation.webengine.context.SharedInformation;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.general.Platform;
import fr.axa.automation.webengine.helper.PropertiesHelperProvider;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.properties.GlobalConfigProperties;
import fr.axa.automation.webengine.util.BrowserFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractStep {

    WebDriver webDriver;
    LoggerService loggerService;

    @Before
    protected void setUp() throws Exception {
        initializeDriver();
        initializeLogger();
    }

    @After
    protected void afterScenario()  throws Exception {
        quiDriver();
    }

    protected void addInformation(String information){
        if(CollectionUtils.isNotEmpty(ExecutionDetail.STEP_IN_PROGRESS)){
            Optional<String> optionalKey = ExecutionDetail.STEP_IN_PROGRESS.stream().reduce((one,two) -> two);
            if(optionalKey.isPresent()){
                String key = optionalKey.get();
                SharedInformation.addInformation(key,information);
            }
        }
    }

    protected void initializeDriver() throws Exception {
        Optional<WebDriver> driver = getDriver();
        if(driver.isPresent()){
            webDriver = driver.get();
        }else{
            throw new Exception("Error during get driver");
        }
    }

    protected Optional<WebDriver> getDriver() throws Exception {
        Optional<GlobalConfigProperties> optionalGlobalConfigProperties = getConfig();
        Optional<WebDriver> driver;
        if(optionalGlobalConfigProperties.isPresent()){
            driver = BrowserFactory.getDriver(optionalGlobalConfigProperties.get());
        }else{
            driver = getDefaultDriver();
        }
        return driver;
    }

    protected Optional<GlobalConfigProperties> getConfig() throws Exception {
        return PropertiesHelperProvider.getInstance().getDefaultGlobalConfiguration();
    }

    protected Optional<WebDriver> getDefaultDriver() throws WebEngineException {
        return BrowserFactory.getWebDriver(Platform.WINDOWS, Browser.CHROMIUM_EDGE);
    }

    protected void initializeLogger() {
        loggerService = new LoggerService();
    }

    protected void quiDriver() throws Exception {
        try {
            getWebDriver().quit();
        }catch (Exception e){
            getLoggerService().warn("Warning during quit browser",e);
            throw  new Exception("test");
        }
    }
}
