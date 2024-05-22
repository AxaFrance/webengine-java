package fr.axa.automation.webengine.xml;


import org.apache.commons.collections4.MapUtils;
import org.glassfish.jaxb.runtime.marshaller.NamespacePrefixMapper;

import java.util.Map;
public class DefaultNamespacePrefixer extends NamespacePrefixMapper {
    public static final String DEFAULT_NAMESPACE = " "; // DEFAULT NAMESPACE
    public static final String DEFAULT_PREFIXE = "";

    private Map<String,String> namespaceAndPrefixMap;

    public DefaultNamespacePrefixer() {
    }

    public DefaultNamespacePrefixer(Map<String,String> namespaceAndPrefixMap) {
        this.namespaceAndPrefixMap = namespaceAndPrefixMap;
    }


    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(MapUtils.isEmpty(namespaceAndPrefixMap)){
            return DEFAULT_PREFIXE;
        }
        return namespaceAndPrefixMap.getOrDefault(namespaceUri,DEFAULT_PREFIXE);
    }
}
