package fr.axa.automation.webengine.xml;


import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

public class NamespacePrefixerWebengine extends NamespacePrefixMapper {
    public static final String DEFAULT_NAMESPACE = " "; // DEFAULT NAMESPACE
    public static final String DEFAULT_PREFIXE = "";

    private Map<String,String> namespaceAndPrefixMap;

    public NamespacePrefixerWebengine() {
    }

    public NamespacePrefixerWebengine(Map<String,String> namespaceAndPrefixMap) {
        this.namespaceAndPrefixMap = namespaceAndPrefixMap;
    }

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if(MapUtils.isEmpty(namespaceAndPrefixMap)){
            return DEFAULT_PREFIXE;
        }
        return namespaceAndPrefixMap.getOrDefault(namespaceUri,DEFAULT_PREFIXE);
    }

//    @Override
//    public String[] getPreDeclaredNamespaceUris() {
//        if(MapUtils.isNotEmpty(namespaceAndPrefixMap)){
//            return Arrays.stream(namespaceAndPrefixMap.keySet().toArray()).toArray(String[]::new);
//        }
//        String[] preDeclaredNamespace = {DEFAULT_NAMESPACE};
//        return preDeclaredNamespace;
//    }
}
