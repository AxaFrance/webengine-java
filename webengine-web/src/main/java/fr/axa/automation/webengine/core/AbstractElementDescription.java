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
        perform(getFunctionInternalClick());
    }

    private Function<Void, Void> getFunctionInternalClick() {
        Function<Void, Void> fun = (x) -> {
            try {
                internalClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    private void internalClick() throws Exception {
        WebElement e = findElement();
        e.click();
    }


    public Byte[] getScreenshot() throws Exception {
        return perform(internalGetScreenshot());
    }

    protected abstract Function<Void, Byte[]> internalGetScreenshot() throws Exception;



    public void sendKeys(String text) throws Exception {
        perform(getFunctionInternalSendKeys(), text);
    }

    private Function<String, Void> getFunctionInternalSendKeys() {
        Function<String, Void> fun = (x) -> {
            try {
                internalSendKeys(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    private void internalSendKeys(String text) throws Exception {
        WebElement e = findElement();
        e.sendKeys(text);
    }

    public void setValue(String text) throws Exception {
        perform(getFunctionInternalSetValue(), text);
    }

    private Function<String, Void> getFunctionInternalSetValue() {
        Function<String, Void> fun = (x) -> {
            try {
                internalSetValue(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    private void internalSetValue(String text) throws Exception {
        WebElement element = internalFindElement();
        element.clear();
        element.sendKeys(text);
    }

    public String getText() throws Exception {
        return perform(getFunctionInternalGetText());
    }

    private Function<Void, String> getFunctionInternalGetText() {
        Function<Void, String> fun = (x) -> {
            String text = null;
            try {
                text = internalGetText();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        };
        return fun;
    }

    private String internalGetText() throws Exception {
        WebElement element = findElement();
        return element.getText();
    }

    public Boolean isSelected() throws Exception {
        return perform(getFunctionInternalIsSelected());
    }

    private Function<Void, Boolean> getFunctionInternalIsSelected() {
        Function<Void, Boolean> fun = (x) -> {
            Boolean text = null;
            try {
                text = internalIsSelected();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        };
        return fun;
    }

    private Boolean internalIsSelected() throws Exception {
        WebElement e = findElement();
        return e.isSelected();
    }

    public Boolean isEnabled() throws Exception {
        return perform(getFunctionInternalIsEnabled());
    }

    private Function<Void, Boolean> getFunctionInternalIsEnabled() {
        Function<Void, Boolean> fun = (x) -> {
            Boolean text = null;
            try {
                text = internalIsEnabled();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        };
        return fun;
    }

    private Boolean internalIsEnabled() throws Exception {
        WebElement e = findElement();
        return e.isEnabled();
    }

    public Boolean isDisplayed() throws Exception {
        return perform(getFunctionInternalIsDisplayed());
    }

    private Function<Void, Boolean> getFunctionInternalIsDisplayed() {
        Function<Void, Boolean> fun = (x) -> {
            Boolean text = null;
            try {
                text = internalIsDisplayed();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        };
        return fun;
    }

    private Boolean internalIsDisplayed() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.SYNCHRONZATION_TIMEOUT);
        WebElement webElement = null;
        boolean value = false;
        while (now.isBefore(timeOut)) {
            WebElement e = findElement();
            value = e.isDisplayed();
            if (!value)
                Thread.sleep(1000);
        }
        return value;
    }


    public void clear() throws Exception {
        perform(getFunctionInternalClear());
    }

    private Function<Void, Void> getFunctionInternalClear() {
        Function<Void, Void> fun = (x) -> {
            try {
                internalClear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    private void internalClear() throws Exception {
        WebElement e = findElement();
        e.clear();
    }


    public String getAttribute(String attributeName) throws Exception {
        return perform(getFunctionInternalGetAttribute(), attributeName);
    }

    private Function<String, String> getFunctionInternalGetAttribute() {
        Function<String, String> fun = (x) -> {
            String value = null;
            try {
                value = internalGetAttribute(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        };
        return fun;
    }

    protected <T, R> R perform(Function<T, R> function) throws Exception {
        R r = perform(function, null);
        return r;
    }

    protected <T, R> R perform(Function<T, R> function, T param) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.SYNCHRONZATION_TIMEOUT);
        R returnValue = null;

//        while (now.isBefore(timeOut) && returnValue==null) {
        try {
            returnValue = function.apply(param);
        } catch (Exception e) {
            throw e;
        }
//        }
        return returnValue;
    }

    public WebElement findElement() throws Exception {
        return findElement(SettingsWeb.SYNCHRONZATION_TIMEOUT);
    }

    public WebElement findElement(int timeoutSecond) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeoutSecond);
        WebElement webElement = null;

        while (now.isBefore(timeOut) && webElement==null) {
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

    private String internalGetAttribute(String attributeName) throws Exception {
        WebElement e = findElement();
        return e.getAttribute(attributeName);
    }

    public Collection<WebElement> findElements(By by) throws Exception {
        WebElement e = findElement();
        return e.findElements(by);
    }

    public Collection<WebElement> findElements() throws Exception {
        return (Collection<WebElement>) findElement(SettingsWeb.SYNCHRONZATION_TIMEOUT);
    }

    public abstract Collection<WebElement> internalFindElements() throws Exception;

    public Collection<WebElement> FindElements(int timeoutSecond) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(SettingsWeb.SYNCHRONZATION_TIMEOUT);
        WebElement webElement = null;
        Boolean value = false;
        Collection<WebElement> webElementCollection = null;
        while (now.isBefore(timeOut)) {
            webElementCollection = internalFindElements();
            if (!value)
                Thread.sleep(1000);
        }
        return webElementCollection;
    }

}
