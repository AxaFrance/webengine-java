package fr.axa.automation.webengine.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.xml.NamespacePrefixerWebengine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class XmlUtil {

    private XmlUtil() {
    }

    public static <T> T unmarshall(String filePath, Class<T> returnType) throws WebEngineException {
        JAXBContext jaxbContext;
        try {
            File file = FileUtil.getFileByPathOrResource(filePath);
            Source source = new StreamSource(file);
            jaxbContext = JAXBContext.newInstance(returnType);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<T> jaxbElement = jaxbUnmarshaller.unmarshal(source, returnType);
            return (T) jaxbElement.getValue();
        } catch (JAXBException | IOException e) {
            throw new WebEngineException("Error during parsing XML data for file : "+filePath, e);
        }
    }

    public static File marshall(InputMarshallDTO inputMarshallDTO) throws WebEngineException {
        JAXBContext jaxbContext;
        Assert.notNull(inputMarshallDTO,"Input parameter is null");
        try {
            File file = FileUtil.getFileByPathOrResource(inputMarshallDTO.getFileDestinationPath());
            Object objectToMarshall = inputMarshallDTO.getObjectToMarshall();
            jaxbContext = JAXBContext.newInstance(objectToMarshall.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, Charset.defaultCharset().name());
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            Optional<NamespacePrefixMapper> namespacePrefixMapper = getNamespacePrefixMapper(inputMarshallDTO);
            if(namespacePrefixMapper.isPresent()){
                jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", namespacePrefixMapper.get());
            }

            QName qname = getQName(inputMarshallDTO);
            JAXBElement jaxbElement = new JAXBElement(qname, objectToMarshall.getClass(), objectToMarshall);
            jaxbMarshaller.marshal(jaxbElement, file);
            return file;
        } catch (JAXBException | IOException e) {
            throw new WebEngineException("Error during parsing XML data for file : "+inputMarshallDTO.getFileDestinationPath(), e);
        }
    }

    private static QName getQName(InputMarshallDTO inputMarshallDTO) {
        String objectToMarshall = inputMarshallDTO.isUpperCaseRootElement() ? inputMarshallDTO.getObjectToMarshall().getClass().getSimpleName() : inputMarshallDTO.getObjectToMarshall().getClass().getSimpleName().toLowerCase();
        return new QName(inputMarshallDTO.getNamespace(), objectToMarshall, inputMarshallDTO.getPrefix());
    }

    private static Optional<NamespacePrefixMapper> getNamespacePrefixMapper(InputMarshallDTO inputMarshallDTO) {
        if (StringUtils.isNotEmpty(inputMarshallDTO.getNamespace()) && StringUtils.isNotEmpty(inputMarshallDTO.getPrefix())) {
            Map<String, String> namespaceAndPrefixMap = new HashMap() ;
            namespaceAndPrefixMap.put(inputMarshallDTO.getNamespace(), inputMarshallDTO.getPrefix());
            return Optional.of(new NamespacePrefixerWebengine(namespaceAndPrefixMap));
        }
        return Optional.empty();
    }
}
