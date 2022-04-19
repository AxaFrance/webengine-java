package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.general.ActionContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassUtil {

    public static <T> T create(Class<T> clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class classToLoad = Class.forName(clazz.getName());
        Object object = (T)classToLoad.newInstance();
        return (T)object;
    }

    public static <T> T create(Class clazz, Class<T> t, Object object,Class<?>... constructorParameters) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = Class.forName(clazz.getName()).getConstructor(constructorParameters);
        return (T)constructor.newInstance(object);
    }

    public static <T> T createAndPopulateAction(Class clazz, String methodName,Object... parameters) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T object = (T) create(clazz);
        object.getClass().getMethod(methodName,new Class[] { ActionContext.class }).invoke(object,parameters[0]);
        return object;
    }
}
