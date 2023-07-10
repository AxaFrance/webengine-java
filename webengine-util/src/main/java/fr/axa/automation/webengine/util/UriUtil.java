package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.net.URISyntaxException;

public class UriUtil {

    public static String getHostFromURI(String url) throws WebEngineException {
        String host = "";
        try {
            java.net.URI uri = new java.net.URI(url);
            host = uri.getHost();
        } catch (URISyntaxException e) {
            throw new WebEngineException("Error while getting host from URI", e);
        }
        return host;
    }


}
