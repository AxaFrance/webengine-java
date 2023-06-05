package fr.axa.automation.webengine.builder;

import fr.axa.automation.webengine.util.FileUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class HtmlBuilder {

    private HtmlBuilder() {
    }

    public static void build(String xmlFileName, String htmlFileName, String xsltResource) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try (InputStream is = new FileInputStream(xmlFileName)) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            try (FileOutputStream output = new FileOutputStream(htmlFileName)) {
                transform(doc, output,xsltResource);
            }
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void transform(Document doc, OutputStream output,String xsltResource) throws TransformerException, FileNotFoundException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new ClasspathResourceURIResolver());
        Transformer transformer = transformerFactory.newTransformer(new StreamSource(FileUtil.getInputStreamFromResource(xsltResource)));
        transformer.transform(new DOMSource(doc), new StreamResult(output));
    }

    private static class ClasspathResourceURIResolver implements URIResolver {
        @Override
        public Source resolve(String href, String base) throws TransformerException {
            return new StreamSource(HtmlBuilder.class.getClassLoader().getResourceAsStream(href));
        }
    }
}
