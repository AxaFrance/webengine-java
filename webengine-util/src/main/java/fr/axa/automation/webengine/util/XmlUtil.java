package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.xml.NamespacePrefixerWebengine;
import org.springframework.util.Assert;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlUtil {

    public static <T> T unmarshall(String filePath, Class<T> returnType) throws WebEngineException {
        JAXBContext jaxbContext;
        try {
            File file = new File(filePath);
            Source source = new StreamSource(file);
            jaxbContext = JAXBContext.newInstance(returnType);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement jaxbElement = jaxbUnmarshaller.unmarshal(source, returnType);
            return (T) jaxbElement.getValue();
        } catch (JAXBException e) {
            throw new WebEngineException("Error during parsing XML data for file : "+filePath, e);
        }
    }

    public static File marshall(InputMarshallDTO inputMarshallDTO) throws WebEngineException {
        JAXBContext jaxbContext;
        Assert.notNull(inputMarshallDTO,"Input parameter is null");
        try {
            File file = new File(inputMarshallDTO.getFileDestinationPath());
            Object objectToMarshall = inputMarshallDTO.getObjectToMarshall();
            jaxbContext = JAXBContext.newInstance(objectToMarshall.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if(inputMarshallDTO.getNamespacePrefixMapper()!=null){
                jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", inputMarshallDTO.getNamespacePrefixMapper());
            }
            String namespaceRoot = inputMarshallDTO.getNamespaceRoot()!=null?inputMarshallDTO.getNamespaceRoot():"";
            QName qname = new QName(namespaceRoot, objectToMarshall.getClass().getSimpleName(),"");
            JAXBElement jaxbElement = new JAXBElement( qname, objectToMarshall.getClass(), objectToMarshall );
            jaxbMarshaller.marshal(jaxbElement, file);
            return file;
        } catch (JAXBException e) {
            throw new WebEngineException("Error during parsing XML data for file : "+inputMarshallDTO.getFileDestinationPath(), e);
        }
    }

//    public static File marshallWithoutNamespace(String filePath, Object object) throws WebEngineException {
//        JAXBContext jaxbContext;
//        try {
//            File file = new File(filePath);
//            jaxbContext = JAXBContext.newInstance(object.getClass());
//            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            JAXBElement jaxbElement = new JAXBElement( new QName("", object.getClass().getSimpleName().toLowerCase(),""), object.getClass(), object );
//            jaxbMarshaller.marshal(jaxbElement, file);
//            return file;
//        } catch (JAXBException e) {
//            throw new WebEngineException("Error during parsing XML data for file : "+filePath, e);
//        }
//    }
//
//
//
//    public static File marshallWithNamespace(String filePath, Object object, String namespace, String prefixe) throws WebEngineException {
//        JAXBContext jaxbContext ;
//        try {
//            File file = new File(filePath);
//            jaxbContext = JAXBContext.newInstance(object.getClass());
//            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            Map<String, String> namespaceAndPrefixMap  = new HashMap() {{put(namespace, prefixe);}};
//            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixerWebengine(namespaceAndPrefixMap));
//            JAXBElement jaxbElement = new JAXBElement( new QName(namespace, object.getClass().getSimpleName(),prefixe), object.getClass(), object );
//            jaxbMarshaller.marshal(jaxbElement, file);
//            return file;
//        } catch (JAXBException e) {
//            throw new WebEngineException("Error during parsing XML data for file : "+filePath, e);
//        }
//    }
}
