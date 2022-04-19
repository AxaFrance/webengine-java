package fr.axa.automation.webengine.util;


import fr.axa.automation.webengine.exception.WebEngineException;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XmlUtil {

    public static <T> T unmarshall(String filePath, Class<T> returnType) throws WebEngineException {
        JAXBContext jaxbContext = null;
        try {
            File file = new File(filePath);
            Source source = new StreamSource(file);
            jaxbContext = JAXBContext.newInstance(returnType);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement jaxbElement = jaxbUnmarshaller.unmarshal(source, returnType);
            return (T) jaxbElement.getValue();
        } catch (JAXBException e) {
            throw new WebEngineException("Error during parsing XML data for file :"+filePath, e);
        }
    }

    public static void marshall(String filePath, Object object) throws WebEngineException {
        JAXBContext jaxbContext = null;
        try {
            File file = new File(filePath);
            Source source = new StreamSource(file);
            jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            JAXBElement jaxbElement = new JAXBElement( new QName("", object.getClass().getSimpleName()), object.getClass(), object );
            jaxbMarshaller.marshal(jaxbElement, file);
        } catch (JAXBException e) {
            throw new WebEngineException("Error during parsing XML data for file :"+filePath, e);
        }
    }



//    public static void main(String args[]) throws Exception {
//        String xmlString =
//                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                        "<EnvironmentVariables xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://www.axa.fr/WebEngine/2022\">\n" +
//                        "    <Variable>\n" +
//                        "        <Name>URL_PROD</Name>\n" +
//                        "        <Value>https://www.google.com/</Value>\n" +
//                        "    </Variable>\n" +
//                        "</EnvironmentVariables>";
//        JAXBContext ctx = JAXBContext.newInstance(EnvironmentVariables.class);
//        Unmarshaller u = ctx.createUnmarshaller();
//        ByteArrayInputStream bais = new ByteArrayInputStream(xmlString.getBytes());
//        Source source = new StreamSource(bais);
//        JAXBElement<EnvironmentVariables> mct = u.unmarshal(source, EnvironmentVariables.class);
//    }

}
