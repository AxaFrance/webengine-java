package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;

@Slf4j
public class JarUtil {

    public static void loadLibrary(java.io.File jar) throws WebEngineException{
        try {
            /*We are using reflection here to circumvent encapsulation; addURL is not public*/
            //java.net.URLClassLoader loader = (java.net.URLClassLoader)ClassLoader.getSystemClassLoader(); //Doesn't work with spring boot
            java.net.URLClassLoader loader = (java.net.URLClassLoader)Thread.currentThread().getContextClassLoader();
            java.net.URL url = jar.toURI().toURL();
            /*Disallow if already loaded*/
            for (java.net.URL it : java.util.Arrays.asList(loader.getURLs())){
                if (it.equals(url)){
                    return;
                }
            }
            java.lang.reflect.Method method = java.net.URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{java.net.URL.class});
            method.setAccessible(true); /*promote the method to public access*/
            method.invoke(loader, new Object[]{url});
        } catch (final java.lang.NoSuchMethodException |
                java.lang.IllegalAccessException |
                java.net.MalformedURLException |
                java.lang.reflect.InvocationTargetException e){
            throw new WebEngineException("Error during loading project "+jar.getAbsolutePath(),e);
        }
    }

    public static <T> Set<Class<? extends T>> findAllClass(Class<T> clazz) {
//        Don't Delete this two lines, need to debug all class from external class loaded
//        Reflections reflections = new Reflections("fr.axa", new SubTypesScanner(false));
//        Set<Class> classes = reflections.getSubTypesOf(Object.class).stream().collect(Collectors.toSet());
        Reflections reflections = new Reflections();
        return reflections.getSubTypesOf(clazz);
    }
}
