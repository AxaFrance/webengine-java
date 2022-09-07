package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.MultipleElementException;
import fr.axa.automation.webengine.general.SettingsWeb;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

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

    protected <T, R> R retry(IFunction<T, R> function, T param) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.TIMEOUT_SECONDES);
        Exception exception = new Exception();

//        while (LocalDateTime.now().isBefore(timeOut)) {
//            try {
//                return function.call(param);
//            } catch (InvalidSelectorException e) {
//                throw e;
//            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException | ElementClickInterceptedException e ) {
//                exception = e;
//                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDES);
//            }
//        }

        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                return function.call(param);
            } catch (Exception e ) {
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

//        while (LocalDateTime.now().isBefore(timeOut)) {
//            try {
//                return internalFindElement();
//            } catch (InvalidSelectorException e) {
//                throw e;
//            } catch (MultipleElementException | NoSuchElementException | StaleElementReferenceException e) {
//                exception = e;
//                waitInMillisecondes(SettingsWeb.WAIT_TIME_MILLISECONDES);
//            }
//        }

        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                return internalFindElement();
            } catch (Exception e) {
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
            return exists(SettingsWeb.TIMEOUT_SECONDES);
        };
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