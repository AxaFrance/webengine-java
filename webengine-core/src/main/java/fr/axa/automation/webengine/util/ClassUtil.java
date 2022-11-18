package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.ActionContext;

import java.lang.reflect.InvocationTargetException;

public class ClassUtil extends CommonClassUtil{

    public static <T> T createAndPopulateAction(Class clazz, String methodName,Object... parameters) throws WebEngineException {
        T object = (T) CommonClassUtil.create(clazz);
        try {
            object.getClass().getMethod(methodName,new Class[] { ActionContext.class }).invoke(object,parameters[0]);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new WebEngineException("Error during call method "+methodName+" instance of class :"+clazz.getSimpleName(),e);
        }
        return object;
    }
}
