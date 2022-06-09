package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.general.SettingsWeb;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractPageModel {

    protected WebDriver useDriver;

    public AbstractPageModel() {
    }

    protected void populateDriver(WebDriver webDriver) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.useDriver = webDriver;
        Class currentClazz = this.getClass();
        List<Field> fieldList = Arrays.asList(currentClazz.getDeclaredFields());
        Type type = null;
        for(Field  field : fieldList) {
            type = field.getType();
            if(type.getTypeName().equalsIgnoreCase("fr.axa.automation.webengine.core.WebElementDescription")){
                Object value = field.get(this);
                field.setAccessible(true);
                ((WebElementDescription)value).populateDriver(webDriver);
            }
        }
    }

    public void waitInMillisecondes(Long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public void sync(long... timeoutInSecond) throws InterruptedException {
        long timeout = SettingsWeb.TIMEOUT_SECONDES;
        if(ArrayUtils.isNotEmpty(timeoutInSecond)){
            timeout = timeoutInSecond[0];
        }

        Wait wait = new WebDriverWait(this.useDriver, timeout);
        wait.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}
