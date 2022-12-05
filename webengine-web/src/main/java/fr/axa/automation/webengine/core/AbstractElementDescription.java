package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.general.SettingsWeb;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Slf4j
@SuperBuilder
public abstract class AbstractElementDescription {
    protected WebDriver useDriver;

    public AbstractElementDescription() {
    }

    public AbstractElementDescription(WebDriver webDriver) {
        this();
        this.useDriver = webDriver;
    }

    public AbstractElementDescription populateDriver(WebDriver webDriver) {
        this.useDriver = webDriver;
        return this;
    }

    public void waitInMillisecondes(Long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    protected <T, R> R perform(Function<T, R> function) throws Exception {
        return perform(function, null);
    }

    protected <T, R> R perform(Function<T, R> function, T param) throws Exception {
        return function.apply(param);
    }

    protected <T, R> R retry(IFunction<T, R> function, T param) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDS);
        Exception exception = new Exception();

        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                return function.call(param);
            } catch (Exception e ) {
                exception = e;
                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDS);
            }
        }

        throw exception;
    }

    protected abstract WebElement internalFindElement() ;

    public WebElement findElement() throws Exception {
        return findElement(SettingsWeb.TIMEOUT_SECONDS);
    }

    public WebElement findElement(int timeOutSecond) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeOutSecond);
        Exception exception = new Exception();

        log.debug("Start find elements. Time is : "+LocalDateTime.now());
        log.debug("Define time out is : "+timeOutSecond);
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement element = internalFindElement();
                log.debug("Element is found at : "+LocalDateTime.now());
                return element;
            } catch (Exception e) {
                exception = e;
                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDS);
            }
        }
        log.debug("End find element and no element found, throw exception at : "+LocalDateTime.now());
        throw exception;
    }

    public Collection<WebElement> findElements(By by) throws Exception {
        WebElement e = findElement();
        return e.findElements(by);
    }

    public WebElement findElement(By by) throws Exception {
        WebElement e = findElement();
        return e.findElement(by);
    }

    public WebElement findElement(By by, int timeoutSecond) throws Exception {
        WebElement e = findElement(timeoutSecond);
        return e.findElement(by);
    }

    public Collection<WebElement> findElements() throws Exception {
        return (Collection<WebElement>) findElement(SettingsWeb.TIMEOUT_SECONDS);
    }

    public abstract Collection<WebElement> internalFindElements() ;

    public Collection<WebElement> findElements(int timeoutSecond) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeoutSecond);
        Collection<WebElement> webElementCollection = null;
        while (LocalDateTime.now().isBefore(timeOut) && webElementCollection == null) {
            webElementCollection = internalFindElements();
        }
        return webElementCollection;
    }

    public Boolean exists() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> exists(SettingsWeb.TIMEOUT_SECONDS);
        return retry(fun,null);
    }

    public boolean exists(int timeoutSecond) {
        try {
            WebElement webElement = findElement(timeoutSecond);
            return webElement != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void click() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
                WebElement webElement = findElement();
                webElement.click();
                return null;
        };
        retry(fun,null);
    }

    public void autocompletion(String text) throws Exception {
        setValue(text);
        pressEnterKey();
    }

    public void pressEnterKey() throws Exception {
        String s = Keys.chord(Keys.RETURN);
        sendKeys(s);
    }

    public void sendKeys(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement webElement = findElement();
            webElement.sendKeys(x);
            return null;
        };
        retry(fun,text);
    }

    public byte[] getScreenshot() throws Exception {
        return perform(internalGetScreenshot());
    }

    protected abstract Function<Void, byte[]> internalGetScreenshot() throws Exception;

    public void setValue(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement element = findElement();
            element.clear();
            element.sendKeys(x);
            return null;
        };
        retry(fun,text);
    }

    public String getText() throws Exception {
        IFunction<Void, String> fun = (x) -> {
            WebElement element = findElement();
            return element.getText();
        };
        return retry(fun,null);
    }

    public Boolean isSelected() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            return webElement.isSelected();
        };
        return retry(fun,null);
    }

    public Boolean isEnabled() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            return webElement.isEnabled();
        };
        return retry(fun,null);
    }

    public Boolean isDisplayed() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            return webElement.isDisplayed();
        };
        return retry(fun,null);
    }

    public void clear() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            webElement.clear();
            return null;
        };
        retry(fun,null);
    }

    public String getAttribute(String attributeName) throws Exception {
        IFunction<String, String> fun = (x) -> {
            WebElement webElement = findElement();
            return webElement.getAttribute(x);
        };
        return retry(fun,attributeName);
    }
}