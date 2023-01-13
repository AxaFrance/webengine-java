package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.general.SettingsWeb;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level= AccessLevel.PROTECTED)
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractPageModel {

    WebDriver useDriver;

    protected void populateDriver(WebDriver webDriver) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        setUseDriver(webDriver);
        Class<? extends AbstractPageModel> currentClazz = this.getClass();
        List<Field> fieldList = Arrays.asList(currentClazz.getDeclaredFields());
        Type type ;
        if(CollectionUtils.isNotEmpty( fieldList)){
            for(Field  field : fieldList) {
                type = field.getType();
                if(type.getTypeName().equalsIgnoreCase("fr.axa.automation.webengine.core.WebElementDescription")){
                    Object value = field.get(this);
                    field.setAccessible(true);
                    ((WebElementDescription)value).populateDriver(webDriver);
                }
            }
        }
    }

    public void waitInMillisecondes(Long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }

    public void sync(long... timeoutInSecond) throws InterruptedException {
        long timeout = SettingsWeb.TIMEOUT_SECONDS;
        if(ArrayUtils.isNotEmpty(timeoutInSecond)){
            timeout = timeoutInSecond[0];
        }

        Wait<WebDriver> wait = new WebDriverWait(this.useDriver, Duration.ofSeconds(timeout));
        wait.until((ExpectedCondition<Boolean>) wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    public void maximize() throws InterruptedException {
        this.getUseDriver().manage().window().maximize();
    }
}
