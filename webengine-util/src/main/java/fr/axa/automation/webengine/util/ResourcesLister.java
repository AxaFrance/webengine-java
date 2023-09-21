package fr.axa.automation.webengine.util;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.util.Set;

public class ResourcesLister {

    public static Set<String> getResources(String pathResource) {
        Reflections reflections = new Reflections(pathResource,new ResourcesScanner());
        return reflections.getResources(s -> true /*match all resources*/);
    }

}
