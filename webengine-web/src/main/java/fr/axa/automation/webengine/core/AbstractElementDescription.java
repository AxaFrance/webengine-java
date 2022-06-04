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
import java.util.function.Consumer;
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

    public void waitFor(Long milliseconds) throws InterruptedException {
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
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
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

    public boolean exists() {
        return exists(SettingsWeb.TIMEOUT_SECONDES);
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
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement webElement = findElement();
                webElement.click();
                return;
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }

    public void sendKeys(String text) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement webElement = findElement();
                webElement.clear();
                webElement.sendKeys(text);
                return;
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }


    public byte[] getScreenshot() throws Exception {
        return perform(internalGetScreenshot());
    }

    protected abstract Function<Void, byte[]> internalGetScreenshot() throws Exception;

    public void setValue(String text) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement element = findElement();
                element.clear();
                element.sendKeys(text);
                return;
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }

    public String getText() throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement element = findElement();
                return element.getText();
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }

    public Boolean isSelected() throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement e = findElement();
                return e.isSelected();
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }


    public Boolean isEnabled() throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement e = findElement();
                return e.isEnabled();
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }

    public Boolean isDisplayed() throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement e = findElement();
                return e.isDisplayed();
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }

    public void clear() throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement e = findElement();
                e.clear();
                return;
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }


    public String getAttribute(String attributeName) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement e = findElement();
                return e.getAttribute(attributeName);
            } catch (InvalidSelectorException e) {
                throw e;
            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
                exception = e;
                waitFor(SettingsWeb.WAIT_TIME_MILLISECONDES);
            }
        }
        throw exception;
    }


}
