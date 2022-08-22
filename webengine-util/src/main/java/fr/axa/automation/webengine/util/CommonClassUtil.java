package fr.axa.automation.webengine.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CommonClassUtil {

    public static <T> T create(Class<T> clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class classToLoad = Class.forName(clazz.getName());
        Object object = (T)classToLoad.newInstance();
        return (T)object;
    }

    public static <T> T create(Class clazz, Class<T> t, Object object,Class<?>... constructorParameters) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = Class.forName(clazz.getName()).getConstructor(constructorParameters);
        return (T)constructor.newInstance(object);
    }
}
