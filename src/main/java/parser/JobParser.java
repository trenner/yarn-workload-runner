package parser;


import Core.Job;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by joh-mue on 03/02/16.
 */
public class JobParser {
    // TODO: Use the logger or get rid of it
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

            ArrayList<Job> jobList = new ArrayList();

            // per Job
            for (Element jobElement : jobs) {
                Job job = new Job();

                job.setJobName(jobElement.getAttribute("name"));

                // Get Runner and parameters
                Node runnerNode = jobElement.getElementsByTagName("runner").item(0);
                parseRunnerInfo(runnerNode, job);

                Node jarNode = jobElement.getElementsByTagName("jar").item(0);
                parseJarInfo(jarNode, job);

                jobList.add(job);
            }
            return jobList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void overwriteJobInfo(Node jobNode, Job job) {
        Element jobElement = (Element) jobNode;

        // Get Runner and parameters
        Node runnerNode = jobElement.getElementsByTagName("runner").item(0);
        if (runnerNode != null) {
//            parseRunnerInfo(runnerNode, job);
        }

        Node jarNode = jobElement.getElementsByTagName("jar").item(0);
        if (jarNode != null) {
//            parseJarInfo(jarNode, job);
        }
    }

    private static void parseRunnerInfo(Node runnerNode, Job job) {
        NodeList runnerChildNodes = runnerNode.getChildNodes();
        for (int i = 0; i < runnerChildNodes.getLength(); i++) {
            Node item = runnerChildNodes.item(i);
            switch (item.getNodeName()) {
                case "name":
                    job.setRunner(item.getTextContent());
                    break;
                case "arguments":
                    job.setRunnerArguments(getArguments(item , "-"));
                    break;
            }

        }
    }

    private static void parseJarInfo(Node jarNode, Job job) {
        // get jar path
        NodeList jarChildNodes = jarNode.getChildNodes();
        for (int i = 0; i < jarChildNodes.getLength(); i++) {
            Node item = jarChildNodes.item(i);
            switch (item.getNodeName()) {
                case "path":
                    job.setJarFile(item.getTextContent());
                    break;
                case "arguments":
                    job.setJarArguments(getArguments(item, ""));
                    break;
            }
        }
    }

    private static ArrayList<String> getArguments(Node node, String separator) {
        ArrayList<String> argumentList = new ArrayList<>();

        NodeList arguments = node.getChildNodes();
        for (int i = 0; i < arguments.getLength(); i++) {
            Node argument = arguments.item(i);
            if (argument.getNodeType() == Node.ELEMENT_NODE) {
                String parameterName = ""; // there might not be a name for the argument
                NamedNodeMap attributes = argument.getAttributes();
                if (attributes.getLength() != 0) {
                    parameterName = separator + argument.getAttributes().item(0).getNodeValue();
                }
                argumentList.add(parameterName + " " + argument.getTextContent());
            }
        }
        return argumentList;
    }
}
