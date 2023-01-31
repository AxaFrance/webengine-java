package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public final class JarUtil {

    private JarUtil() {
    }

    public static void loadLibrary(File jar) throws WebEngineException{
        try {
            URLClassLoader urlClassloader = (URLClassLoader)Thread.currentThread().getContextClassLoader();  /*We are using reflection here to circumvent encapsulation; addURL is not public*/
            URL urlJar = jar.toURI().toURL();
            Collection<URL> urlList = Arrays.asList(urlClassloader.getURLs()).stream().filter(url -> url.equals(urlJar)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(urlList)){
                return; /*Disallow if already loaded*/
            }
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true); /*promote the method to public access*/
            method.invoke(urlClassloader, new Object[]{urlJar});
        } catch (NoSuchMethodException | IllegalAccessException | MalformedURLException | InvocationTargetException e){
            throw new WebEngineException("Error during loading project "+jar.getAbsolutePath(),e);
        }
    }

}
