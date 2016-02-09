package parser;

import Core.Job;
import org.w3c.dom.*;

import org.apache.log4j.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

import Core.FlinkJob;

/**
 * Created by Johannes on 03/02/16.
 */
public class JobParser {
    private static final Logger LOG = Logger.getLogger(JobParser.class);

    public static ArrayList<Job> parseJobs(File jobFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(jobFile);
            doc.getDocumentElement().normalize();

            NodeList jobNodeList = doc.getElementsByTagName("job");

            ArrayList<Element> jobs = new ArrayList<>();

            for (int i = 0; i < jobNodeList.getLength(); i++) {
                Node node = jobNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    jobs.add((Element) node);
                }
            }

            ArrayList<Job> jobList = new ArrayList<>();

            // per Job
            for (Element jobElement : jobs) {
                // TODO: get framework and create appropriate job
                FlinkJob flinkJob = new FlinkJob();
                // get jar path
                flinkJob.setJarFile(jobElement.getElementsByTagName("jar").item(0).getTextContent());
                // get parameters
                // there is only 1 "parameters" node. it contains multiple parameter-elements
                NodeList parameters = jobElement.getElementsByTagName("parameters").item(0).getChildNodes();
                for (int i = 0; i < parameters.getLength(); i++) {
                    Node parameter = parameters.item(i);
                    if (parameter.getNodeType() == Node.ELEMENT_NODE) {
                        String argument = parameter.getAttributes().item(0).getNodeValue();
                        // String argument = p.toString();
                        String value = parameter.getTextContent();
                        flinkJob.addParameter(argument, value);
                    }
                }
                System.out.println(flinkJob.getCommand());
                jobList.add(flinkJob);
            }

            System.out.println(jobList);
            return jobList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: refactor, this should never be reached, thus not necessary
        return null;
    }
}
