package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ConfigurationSSL {

    public static void configureSSL()throws WebEngineException {
        TrustManager[] trustAllCerts = getTrustManagers();
        try {
//            Activate the new trust managertry
//            SSLContext sc = SSLContext.getInstance("SSL");
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            throw new WebEngineException("Error during configuring SSL",e);
        }
    }


    private static TrustManager[] getTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
    }
}
