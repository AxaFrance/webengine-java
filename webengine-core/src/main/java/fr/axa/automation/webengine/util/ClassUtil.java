package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.general.ActionContext;

import java.lang.reflect.InvocationTargetException;

public class ClassUtil extends CommonClassUtil{

    public static <T> T createAndPopulateAction(Class clazz, String methodName,Object... parameters) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T object = (T) create(clazz);
        object.getClass().getMethod(methodName,new Class[] { ActionContext.class }).invoke(object,parameters[0]);
        return object;
    }
}
