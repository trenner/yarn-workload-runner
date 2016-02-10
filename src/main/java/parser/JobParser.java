package parser;

import Core.FlinkJob;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import util.JobList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Johannes on 03/02/16.
 */
public class JobParser {
    private static final Logger LOG = Logger.getLogger(JobParser.class);

    public static JobList parseJobs(File jobFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(jobFile);
            doc.getDocumentElement().normalize();

            NodeList jobNodeList = doc.getElementsByTagName("job");
            // TODO: this seems redundant, get it straight from the NodeList
            ArrayList<Element> jobs = new ArrayList<>();
            for (int i = 0; i < jobNodeList.getLength(); i++) {
                Node node = jobNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    jobs.add((Element) node);
                }
            }

            JobList jobList = new JobList();

            // per Job
            for (Element jobElement : jobs) {
                // TODO: get framework and create appropriate job
                FlinkJob flinkJob = new FlinkJob();
                // get job name
                flinkJob.setJobName(jobElement.getAttribute("name"));
                // get jar path
                flinkJob.setJarFile(jobElement.getElementsByTagName("jar").item(0).getTextContent());
                // get parameters
                // there is only 1 "parameters" node. it contains multiple parameter-elements
                NodeList parameters = jobElement.getElementsByTagName("parameters").item(0).getChildNodes();
                for (int i = 0; i < parameters.getLength(); i++) {
                    Node parameter = parameters.item(i);
                    if (parameter.getNodeType() == Node.ELEMENT_NODE) {
                        String parameterName = "";
                        NamedNodeMap attributes = parameter.getAttributes();
                        if (attributes.getLength() != 0) {
                            parameterName = parameter.getAttributes().item(0).getNodeValue();
                        }
                        flinkJob.addParameter(parameterName, parameter.getTextContent());
                    }
                }
                jobList.add(flinkJob);
            }
            return jobList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: refactor, this should never be reached, thus not necessary
        return null;
    }
}
