package fr.axa.automation.webengine;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HtmlBuilder {

    private static final String XML_FILENAME = "C:\\Temp\\html-report\\report-complexe-with-id.xml";
    private static final String XSLT_FILENAME = "webengine-report/src/main/resources/html-report/xslt/index.xslt";

    public static void build(String xmlFileName, String htmlFileName, InputStream xsltStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try (InputStream is = new FileInputStream(xmlFileName)) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            try (FileOutputStream output = new FileOutputStream(htmlFileName)) {
                transform2(doc, output, xsltStream);
            }
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void transform(Document doc, OutputStream output, InputStream xsltStream) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsltStream));
        transformer.transform(new DOMSource(doc), new StreamResult(output));
    }

    private static void transform2(Document doc, OutputStream output, InputStream xsltStream) throws TransformerException {
        TransformerFactory transFact = TransformerFactory.newInstance();
        StreamSource xsltSource = new StreamSource(xsltStream);

        // XXX for 'xsl:import' to load other xsls from class path
        transFact.setURIResolver(new ClasspathResourceURIResolver());
        Templates cachedXSLT = transFact.newTemplates(xsltSource);
        Transformer transformer = cachedXSLT.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(output));
    }

    private static class ClasspathResourceURIResolver implements URIResolver {
        @Override
        public Source resolve(String href, String base) throws TransformerException {
            return new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(href));
        }
    }

}
