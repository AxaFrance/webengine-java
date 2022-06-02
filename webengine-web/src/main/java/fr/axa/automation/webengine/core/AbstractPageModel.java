package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.util.ClassUtil;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractPageModel {

    public AbstractPageModel() {
    }

    protected void populateDriver(WebDriver webDriver) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class currentClazz = this.getClass();
        List<Field> fieldList = Arrays.asList(currentClazz.getDeclaredFields());
        Type type = null;
        for(Field  field : fieldList) {
            type = field.getType();
            if(type.getTypeName().equalsIgnoreCase("fr.axa.automation.webengine.core.WebElementDescription")){
                Object value = field.get(this);
                field.setAccessible(true);
                ((WebElementDescription)value).useDriver(webDriver);

            }
        }
    }

}
