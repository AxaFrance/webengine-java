package fr.axa.automation.webengine.report.builder;

import fr.axa.automation.webengine.constant.JavaConstant;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.report.constante.XsltFileConstant;
import fr.axa.automation.webengine.util.FileUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public final class HtmlBuilder {
    private HtmlBuilder() {
    }
    private static final ILoggerService loggerService = new LoggerService();

    public static void build(String xmlFileName, String htmlFileName, String basePath, String xsltResource) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try (InputStream is = new FileInputStream(xmlFileName)) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            try (FileOutputStream output = new FileOutputStream(htmlFileName)) {
                transform(doc, output,basePath,xsltResource);
            }
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void transform(Document doc, OutputStream output, String basePath,String xsltResource) throws TransformerException, FileNotFoundException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setURIResolver(new ClasspathResourceURIResolver());
        Source source = new StreamSource(FileUtil.getInputStreamFromResource(xsltResource));
        source.setSystemId(JavaConstant.CLASSPATH + basePath);
        Transformer transformer = transformerFactory.newTransformer(source);
        transformer.transform(new DOMSource(doc), new StreamResult(output));
    }

    private static class ClasspathResourceURIResolver implements URIResolver {
        @Override
        public Source resolve(String href, String base) throws TransformerException {
            List<String> resourceList = XsltFileConstant.XSLT_FILE_LIST.getValue();
            Path p = Paths.get(href);
            String xsltFile = p.getFileName().toString();
            List<String> fileList = resourceList.stream().filter(s -> s.endsWith("/"+xsltFile)).collect(Collectors.toList());
            return new StreamSource(HtmlBuilder.class.getClassLoader().getResourceAsStream(fileList.get(0)));
        }
    }
}
