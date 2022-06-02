package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.general.SettingsWeb;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    abstract WebElement internalFindElement() throws Exception;

    public AbstractElementDescription useDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        return this;
    }

    public void wait(Long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    protected <T, R> R perform(Function<T, R> function) throws Exception {
        R r = perform(function, null);
        return r;
    }

    protected <T, R> R perform(Function<T, R> function, T param) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.SYNCHRONZATION_TIMEOUT);
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

    public boolean exists() {
        return exists(SettingsWeb.SYNCHRONZATION_TIMEOUT);
    }

    public boolean exists(int timeoutSecond) {
        try {
            findElement(timeoutSecond);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void click() throws Exception {
        WebElement e = findElement();
        e.click();
    }

    public void click(Long...millisecondes) throws Exception {
        click();
        wait(millisecondes[0]);
    }

    public Byte[] getScreenshot() throws Exception {
        return perform(internalGetScreenshot());
    }

    protected abstract Function<Void, Byte[]> internalGetScreenshot() throws Exception;

    public void sendKeys(String text) throws Exception {
        WebElement e = findElement();
        e.sendKeys(text);
    }

    public void sendKeys(String text,Long... millisecondes) throws Exception {
        sendKeys(text);
        wait(millisecondes[0]);
    }

    public void setValue(String text) throws Exception {
        WebElement element = findElement();
        element.clear();
        element.sendKeys(text);
    }

    public String getText() throws Exception {
        WebElement element = findElement();
        return element.getText();
    }

    public Boolean isSelected() throws Exception {
        WebElement e = findElement();
        return e.isSelected();
    }

    public Boolean isEnabled() throws Exception {
        WebElement e = findElement();
        return e.isEnabled();
    }

    public Boolean isDisplayed() throws Exception {
            WebElement e = findElement();
            return e.isDisplayed();
    }

//    public Boolean isDisplayed() throws Exception {
//        return perform(getFunctionInternalIsDisplayed());
//    }
//
//    private Function<Void, Boolean> getFunctionInternalIsDisplayed() {
//        Function<Void, Boolean> fun = (x) -> {
//            Boolean text = null;
//            try {
//                text = internalIsDisplayed();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return text;
//        };
//        return fun;
//    }
//
//    private Boolean internalIsDisplayed() throws Exception {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.SYNCHRONZATION_TIMEOUT);
//        WebElement webElement = null;
//        boolean value = false;
//        while (now.isBefore(timeOut)) {
//            WebElement e = findElement();
//            value = e.isDisplayed();
//            if (!value)
//                Thread.sleep(1000);
//        }
//        return value;
//    }


    public void clear() throws Exception {
        WebElement e = findElement();
        e.clear();
    }

    public String getAttribute(String attributeName) throws Exception {
        WebElement e = findElement();
        return e.getAttribute(attributeName);
    }

    public WebElement findElement() throws Exception {
        return findElement(SettingsWeb.SYNCHRONZATION_TIMEOUT);
    }

    public WebElement findElement(int timeoutSecond) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeoutSecond);
        WebElement webElement = null;

        while (LocalDateTime.now().isBefore(timeOut) && webElement==null) {
            try {
                webElement = internalFindElement();
            } catch (Exception e) {
                throw e;
            }
        }
        return webElement;
    }

    public WebElement findElement(By by) throws Exception {
        WebElement e = findElement();
        return e.findElement(by);
    }

    public WebElement findElement(By by, int timeoutSecond) throws Exception {
        WebElement e = findElement(timeoutSecond);
        return e.findElement(by);
    }

    public Collection<WebElement> findElements(By by) throws Exception {
        WebElement e = findElement();
        return e.findElements(by);
    }

    public Collection<WebElement> findElements() throws Exception {
        return (Collection<WebElement>) findElement(SettingsWeb.SYNCHRONZATION_TIMEOUT);
    }

    public abstract Collection<WebElement> internalFindElements() throws Exception;

    public Collection<WebElement> findElements(int timeoutSecond) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeoutSecond);
        Collection<WebElement> webElementCollection = null;
        while (LocalDateTime.now().isBefore(timeOut) && webElementCollection==null) {
            webElementCollection = internalFindElements();
        }
        return webElementCollection;
    }

}
