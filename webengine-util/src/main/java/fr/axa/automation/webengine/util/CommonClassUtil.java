package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CommonClassUtil {

    public static <T> T create(Class<T> clazz) throws WebEngineException {
        Class classToLoad = null;
        Object object = null;
        try {
            classToLoad = Class.forName(clazz.getName());
            object = (T)classToLoad.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new WebEngineException("Error during create instance of class :"+clazz.getSimpleName(),e);
        }

        return (T)object;
    }

    public static <T> T create(Class clazz, Class<T> t, Object object,Class<?>... constructorParameters) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = Class.forName(clazz.getName()).getConstructor(constructorParameters);
        return (T)constructor.newInstance(object);
    }
}
