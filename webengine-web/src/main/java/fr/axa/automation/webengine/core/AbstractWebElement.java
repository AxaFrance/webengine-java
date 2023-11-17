package fr.axa.automation.webengine.core;


import fr.axa.automation.webengine.api.IFunction;
import fr.axa.automation.webengine.constante.HtmlAttributeConstant;
import fr.axa.automation.webengine.global.SettingsWeb;
import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

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

        log.debug("Search element : "+toString());
        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                WebElement element = internalFindElement();
                if(element!=null){
                    log.debug("Found element : "+toString());
                }
                return element;
            } catch (Exception e) {
                exception = e;
                log.debug("Retry to search element : "+toString());
                waitInMillisecondes(SettingsWeb.RETRY_MILLISECONDS);
            }
        }
        log.debug("Timeout for element : "+toString());
        throw exception;
    }

    public Collection<WebElement> findElements(By by) throws Exception {
        IFunction<By ,Collection<WebElement>> fun = (param) -> getUseDriver().findElements(param);
        return retry(fun,by);
    }

    public WebElement findElement(By by) throws Exception {
        return findElement(by,SettingsWeb.TIMEOUT_SECONDS);
    }

    public WebElement findElement(By by, int timeoutSecond) throws Exception {
        IFunction<By ,WebElement> fun = (param) -> getUseDriver().findElement(param);
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
        IFunction<Integer, Boolean> fun = (param) -> existWithoutRetry(param.intValue());
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
        IFunction<Void, Void> fun = (param) -> {
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
        IFunction<String, Void> fun = (sendKeysValue) -> {
            WebElement webElement = findElement();
            sendKeys(sendKeysValue, webElement);
            return null;
        };
        retry(fun,text);
    }

    protected void sendKeys(String sendKeysValue, WebElement webElement) {
        webElement.sendKeys(sendKeysValue);
    }

    protected void sendKeysWithClear(String sendKeysValue, WebElement webElement) {
        webElement.clear();
        webElement.sendKeys(sendKeysValue);
    }

    public void setValue(String text) throws Exception {
        IFunction<String, Void> fun = (param) -> {
            WebElement element = findElement();
            element.clear();
            sendKeys(param, element);
            return null;
        };
        retry(fun,text);
    }

    public String getText() throws Exception {
        IFunction<Void, String> fun = (param) -> {
            WebElement element = findElement();
            return element.getText();
        };
        return retry(fun,null);
    }

    public Boolean isNotSelected() throws Exception {
        return !isSelected();
    }

    public Boolean isSelected() throws Exception {
        IFunction<Void, Boolean> fun = (param) -> {
            WebElement webElement = findElement();
            if(webElement.isEnabled() && webElement.isDisplayed()){
                return webElement.isSelected();
            }
            throw new Exception("Element is not enabled or displayed");
        };
        return retry(fun,null);
    }

    public Boolean isEnabled() throws Exception {
        IFunction<Void, Boolean> fun = (param) -> {
            WebElement webElement = findElement();
            return webElement.isEnabled();
        };
        return retry(fun,null);
    }

    public Boolean isNotDisplayed() throws Exception {
        return !isDisplayed();
    }

    public Boolean isDisplayed() throws Exception {
        IFunction<Void, Boolean> fun = (param) -> {
            WebElement webElement = findElement();
            return webElement.isDisplayed();
        };
        return retry(fun,null);
    }

    protected static void assertInputValue(WebElement webElement, String expectedValue) throws Exception {
        String actualValue = webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_VALUE.getValue());
        if(!StringUtil.equalsIgnoreCase(expectedValue,actualValue)){
            throw new Exception("The expected value is :'" + expectedValue +"' and the actual value is :'" + actualValue + "'");
        }
    }

    public void clear() throws Exception {
        IFunction<Void, Void> fun = (param) -> {
            WebElement webElement = findElement();

            webElement.clear();

            String st = Keys.chord(Keys.CONTROL, "a");
            webElement.sendKeys(st);
            webElement.sendKeys(Keys.DELETE);

            Actions actions = new Actions(useDriver);
            actions.doubleClick(webElement).perform();
            webElement.sendKeys(Keys.BACK_SPACE);

            assertInputValue(webElement, StringUtils.EMPTY);
            return null;
        };
        retry(fun,null);
    }

    public void clearWithKey() throws Exception {
        IFunction<Void, Void> fun = (param) -> {
            WebElement webElement = findElement();
            String st = Keys.chord(Keys.CONTROL, "a");
            webElement.sendKeys(st);
            webElement.sendKeys(Keys.DELETE);
            return null;
        };
        retry(fun,null);
    }

    public String getAttribute(String attributeName) throws Exception {
        IFunction<String, String> fun = (param) -> {
            WebElement webElement = findElement();
            return webElement.getAttribute(param);
        };
        return retry(fun,attributeName);
    }
}