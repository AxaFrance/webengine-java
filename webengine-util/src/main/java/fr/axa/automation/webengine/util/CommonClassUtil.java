package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public final class CommonClassUtil {

    private CommonClassUtil() {
    }

    public static <T> T create(Class<T> clazz) throws WebEngineException {
        Class classToLoad;
        Object object;
        try {
            classToLoad = Class.forName(clazz.getName());
            object = (T)classToLoad.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new WebEngineException("Error during create instance of class :"+clazz.getSimpleName(),e);
        }
        return (T)object;
    }

    public static <T> T create(Class<T> clazz, Object[] object,Class[] constructor1) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = Class.forName(clazz.getName()).getDeclaredConstructor(constructor1);
        return (T)constructor.newInstance(object);
    }

    public static <T> T createAndCallMethod(Class clazz, String methodName,Object... parameters) throws WebEngineException {
        T object = (T) create(clazz);
        try {
            List<Class> parameterTypeList = ListUtil.getClasses(parameters);
            Class[] parameterTypeArray = parameterTypeList.stream().toArray(c -> new Class[c]);
            Method method = object.getClass().getMethod(methodName,parameterTypeArray);
            method.invoke(object,parameters);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IllegalArgumentException e) {
            throw new WebEngineException("Error during call method "+methodName+" instance of class :"+clazz.getSimpleName(),e);
        }
        return object;
    }

    public static <T> Set<Class<? extends T>> findAllClass(Class<T> clazz) {
        Reflections reflections = new Reflections();
        return reflections.getSubTypesOf(clazz);
    }
}
