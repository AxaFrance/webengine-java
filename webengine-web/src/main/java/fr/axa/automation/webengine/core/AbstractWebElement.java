package fr.axa.automation.webengine.core;


import fr.axa.automation.webengine.api.IFunction;
import fr.axa.automation.webengine.global.SettingsWeb;
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

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Slf4j
@SuperBuilder
public abstract class AbstractWebElement extends AbstractElement{

    public AbstractWebElement() {
        super();
    }

    public AbstractWebElement(WebDriver webDriver) {
        super(webDriver);
    }

    protected abstract WebElement internalFindElement() throws Exception;

    public abstract Collection<WebElement> internalFindElements() throws Exception;

    public WebElement findElement() throws Exception {
        return findElement(SettingsWeb.TIMEOUT_SECONDS);
    }

    public WebElement findElement(int timeOutSecond) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeOutSecond);
        Exception exception = new Exception();
        UUID uuid = UUID.randomUUID();

        log.debug(uuid+"-Element search at "+LocalDateTime.now()+". Defined time out is :"+timeOutSecond);
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement element = internalFindElement();
                if(element!=null){
                    log.debug(uuid+"-Element founded at "+LocalDateTime.now());
                }
                return element;
            } catch (Exception e) {
                exception = e;
                waitInMillisecondes(SettingsWeb.RETRY_MILLISECONDS);
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
        return retry(fun,by,timeoutSecond);
    }

    public Collection<WebElement> findElements() throws Exception {
        return (Collection<WebElement>) findElement(SettingsWeb.TIMEOUT_SECONDS);
    }

    public Boolean isNotExists() throws Exception {
        return !exists();
    }

    public Boolean exists() throws Exception {
        return exists(Integer.valueOf(SettingsWeb.TIMEOUT_SECONDS));
    }

    public Boolean exists(Integer timeoutSecond) throws Exception {
        IFunction<Integer, Boolean> fun = (x) -> existWithoutRetry(x.intValue());
        return retry(fun,timeoutSecond);
    }

    protected boolean existWithoutRetry(int timeoutSecond) {
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
            click(webElement);
            return null;
        };
        retry(fun, null);
    }

    protected void click(WebElement webElement) {
        webElement.click();
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
            sendKeys(x, webElement);
            return null;
        };
        retry(fun,text);
    }

    protected void sendKeys(String x, WebElement webElement) {
        webElement.sendKeys(x);
    }

    protected void sendKeysWithClear(String x, WebElement webElement) {
        webElement.clear();
        webElement.sendKeys(x);
    }

    public void setValue(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement element = findElement();
            element.clear();
            sendKeys(x, element);
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

    public Boolean isNotSelected() throws Exception {
        return !isSelected();
    }

    public Boolean isSelected() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            if(webElement.isEnabled() && webElement.isDisplayed()){
                return webElement.isSelected();
            }
            throw new Exception("Element is not enabled or displayed");
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

    public Boolean isNotDisplayed() throws Exception {
        return !isDisplayed();
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