package parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

/**
 * Created by Johannes on 03/02/16.
 */
public class ConfigParser {

    public static HashMap parseConfig(File configFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(configFile);
            doc.getDocumentElement().normalize();

            NodeList configNodes = doc.getChildNodes().item(0).getChildNodes();

            HashMap<String, String> config = new HashMap<>();
            for (int i = 0; i < configNodes.getLength(); i++) {
                if (configNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    config.put(configNodes.item(i).getNodeName(), configNodes.item(i).getTextContent());
                }
            }

            return config;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
