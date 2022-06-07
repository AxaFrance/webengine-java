package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.MultipleElementException;
import fr.axa.automation.webengine.general.SettingsWeb;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Slf4j
public abstract class AbstractElementDescription {
    protected WebDriver webDriver;

    public AbstractElementDescription() {
    }

    public AbstractElementDescription(WebDriver webDriver) {
        this.webDriver = webDriver;
    }


    public AbstractElementDescription useDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        return this;
    }

    public void waitInMillisecondes(Long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    protected <T, R> R perform(Function<T, R> function) throws Exception {
        R r = perform(function, null);
        return r;
    }

    protected <T, R> R perform(Function<T, R> function, T param) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        R returnValue = null;

        while (LocalDateTime.now().isBefore(timeOut) && returnValue == null) {
            try {
                returnValue = function.apply(param);
            } catch (Exception e) {
                throw e;
            }
        }
        return returnValue;
    }

    protected <T, R> R perform2(IFunction<T, R> function, T param) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();

        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                R r = function.call(param);
                return r;
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }

    protected abstract WebElement internalFindElement() ;

    public WebElement findElement() throws Exception {
        return findElement(SettingsWeb.TIMEOUT_SECONDES);
    }

    public WebElement findElement(int timeoutSecond) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeoutSecond);
        Exception exception = new Exception();

        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                return internalFindElement();
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
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
        return (Collection<WebElement>) findElement(SettingsWeb.TIMEOUT_SECONDES);
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
        IFunction<Void, Boolean> fun = (x) -> {
            return Boolean.valueOf(exists(SettingsWeb.TIMEOUT_SECONDES));
        };
        return perform2(fun,null);
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
        perform2(fun,null);
    }

    public void sendKeys(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement webElement = findElement();
            webElement.clear();
            webElement.sendKeys(x);
            return null;
        };
        perform2(fun,text);
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
        perform2(fun,text);
    }

    public String getText() throws Exception {
        IFunction<Void, String> fun = (x) -> {
            WebElement element = findElement();
            return element.getText();
        };
        return perform2(fun,null);
    }

    public Boolean isSelected() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement e = findElement();
            return Boolean.valueOf(e.isSelected());
        };
        return perform2(fun,null);
    }

    public Boolean isEnabled() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement e = findElement();
            return Boolean.valueOf(e.isEnabled());
        };
        return perform2(fun,null);
    }

    public Boolean isDisplayed() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement e = findElement();
            return Boolean.valueOf(e.isDisplayed());
        };
        return perform2(fun,null);
    }

    public void clear() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement e = findElement();
            e.clear();
            return null;
        };
        perform2(fun,null);
    }

    public String getAttribute(String attributeName) throws Exception {
        IFunction<String, String> fun = (x) -> {
            WebElement e = findElement();
            return e.getAttribute(x);
        };
        return perform2(fun,attributeName);
    }
}