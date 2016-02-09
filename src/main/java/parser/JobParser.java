package parser;

import org.w3c.dom.Document;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

import Core.FlinkJob;

/**
 * Created by Johannes on 03/02/16.
 */
public class JobParser {

    public static void main(String[] args) {
        parseJobs(new File("/Users/Johannes/arbeit/yarn-timed-workload-generator/src/main/resources/jobs.xml"));
    }

    private static final Logger LOG = Logger.getLogger(JobParser.class);

    public static void parseJobs(File jobFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(jobFile);

            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nodeList = doc.getElementsByTagName("job");

            ArrayList<Element> jobs = new ArrayList<Element>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    jobs.add((Element) node);
                }
            }

            for (Element job : jobs) {
                FlinkJob flinkJob = new FlinkJob();
                flinkJob.setJarFile(job.getElementsByTagName("jar").item(0).getTextContent());
                // get parameters
                NodeList parameters = job.getElementsByTagName("parameters");
                for (int i = 0; i < parameters.getLength(); i++) {
                    Node parameter = parameters.item(i);
                    if (parameter.getNodeType() == Node.ELEMENT_NODE) {
                        String argument = parameter.getAttributes().;
                        flinkJob.addArgument(argument, value);
                    }
                }

                flinkJob.setDelay(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
