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
import java.util.UUID;
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


    protected abstract WebElement internalFindElement() ;

    public abstract Collection<WebElement> internalFindElements() ;

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
        UUID uuid = UUID.randomUUID();

        log.debug(uuid+"-retry started  "+function.toString()+" at "+LocalDateTime.now()+". Defined time out is :"+timeOut);
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                R r = function.call(param);
                log.debug(uuid+"-retry succes "+function.toString()+" at "+LocalDateTime.now());
                return r;
            } catch (Exception e ) {
                exception = e;
                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDS);
            }
            log.debug(uuid+"-retry timeout "+function.toString()+" at "+LocalDateTime.now());
        }
        throw exception;
    }

    public WebElement findElement() throws Exception {
        return findElement(SettingsWeb.TIMEOUT_SECONDS);
    }

    public WebElement findElement(int timeOutSecond) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeOutSecond);
        Exception exception = new Exception();
        UUID uuid = UUID.randomUUID();

        log.debug(uuid+"-Element search  "+toString()+" at "+LocalDateTime.now()+". Defined time out is :"+timeOutSecond);
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement element = internalFindElement();
                if(element!=null){
                    log.debug(uuid+"-Element founded "+toString()+" at "+LocalDateTime.now());
                }
                return element;
            } catch (Exception e) {
                exception = e;
                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDS);
            }
        }
        log.debug(uuid+"-Time out search "+LocalDateTime.now());
        throw exception;
    }

    public Collection<WebElement> findElements(By by) throws Exception {
        IFunction<By ,Collection<WebElement>> fun = (x) -> getUseDriver().findElements(x);
        return retry(fun,by);
    }

    public WebElement findElement(By by) throws Exception {
        return findElement(by,SettingsWeb.TIMEOUT_SECONDS);
    }

    public WebElement findElement(By by, int timeoutSecond) throws Exception {
        IFunction<By ,WebElement> fun = (x) -> getUseDriver().findElement(x);
        return retry(fun,by);
    }

    public Collection<WebElement> findElements() throws Exception {
        return (Collection<WebElement>) findElement(SettingsWeb.TIMEOUT_SECONDS);
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