package eu.tomylobo.spaceengineers.util;

import com.jcabi.xml.XML;
import com.jme3.math.ColorRGBA;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import eu.tomylobo.spaceengineers.Statistics;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static final String SAVE_FILE_NAME_SBS = "SANDBOX_0_0_0_.sbs";

    public static Document getDOM(String saveName) throws ParserConfigurationException, SAXException, IOException {
        final String saveFileName = getSaveFileName(saveName);
        final File sourceFile = new File(saveFileName);
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.parse(sourceFile);
    }


    public static void dumpNode(Node node) throws TransformerException {
        dumpNode(new DOMSource(node), new StreamResult(System.out));
    }

    public static void dumpNode(Source source, Result result) throws TransformerException {
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(source, result);
    }

    public static void dumpNode2(Node node) throws TransformerException {
        System.out.println(((CoreDocumentImpl) node.getOwnerDocument()).saveXML(node));
        //System.out.println(new XMLDocument(node).toString());
    }

    public static void dumpNode2(XML node) throws TransformerException {
        System.out.println(node);
        //dumpNode2(node.node());
    }


    public static void transform(String xsltResourceFileName, Source xmlSource, Result xmlResult) throws TransformerException {
        transform(new StreamSource(Statistics.class.getResourceAsStream(xsltResourceFileName)), xmlSource, xmlResult);
    }

    public static void transform(Source xsltSource, Source xmlSource, Result xmlResult) throws TransformerException {
        final TransformerFactory factory = TransformerFactory.newInstance();
        final Transformer transformer = factory.newTransformer(xsltSource);
        transformer.transform(xmlSource, xmlResult);
    }


    public static Source getSaveSource(String saveName) throws FileNotFoundException {
        return new StreamSource(new FileInputStream(getSaveFileName(saveName)));
    }

    public static String getSaveFileName(String saveName) {
        return saveName + File.separator + SAVE_FILE_NAME_SBS;
    }


    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();
    private static final Map<String, XPathExpression> cache = new HashMap<>();

    public static List<Node> xPathNodes(Node node, String expression) throws XPathExpressionException {
        return IterableNodeList.create((NodeList) getXPathExpression(expression).evaluate(node, XPathConstants.NODESET));
    }

    public static String xPath(Node node, String expression) throws XPathExpressionException {
        return (String) getXPathExpression(expression).evaluate(node, XPathConstants.STRING);
    }

    public static XPathExpression getXPathExpression(String expression) throws XPathExpressionException {
        XPathExpression xPathExpression = cache.get(expression);
        if (xPathExpression == null) {
            cache.put(expression, xPathExpression = X_PATH_FACTORY.newXPath().compile(expression));
        }
        return xPathExpression;
    }

    private static ColorRGBA hsvToColorRGBA(float hue, float saturation, float value) {
        final ColorRGBA colorRGBA = new ColorRGBA();
        colorRGBA.fromIntRGBA(Color.HSBtoRGB(hue, saturation, value));
        return colorRGBA;
    }
}
