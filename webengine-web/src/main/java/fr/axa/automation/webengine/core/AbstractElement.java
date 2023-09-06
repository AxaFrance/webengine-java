package fr.axa.automation.webengine.core;


import fr.axa.automation.webengine.api.IFunction;
import fr.axa.automation.webengine.global.SettingsWeb;
import fr.axa.automation.webengine.util.FunctionUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Slf4j
@SuperBuilder
public abstract class AbstractElement {
    protected WebDriver useDriver;

    public AbstractElement() {
    }

    public AbstractElement(WebDriver webDriver) {
        this();
        this.useDriver = webDriver;
    }

    public AbstractElement populateDriver(WebDriver webDriver) {
        this.useDriver = webDriver;
        return this;
    }

    public void waitInMillisecondes(Long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public byte[] getScreenshot() throws Exception {
        return FunctionUtil.perform(internalGetScreenshot());
    }

    protected abstract Function<Void, byte[]> internalGetScreenshot() throws Exception;

    protected <T, R> R retry(IFunction<T, R> function, T param) throws Exception {
        return retry(function,param,SettingsWeb.TIMEOUT_SECONDS);
    }

    protected <T, R> R retry(IFunction<T, R> function, T param, Integer timeout) throws Exception {
        return FunctionUtil.retry(function, param, timeout);
    }
}