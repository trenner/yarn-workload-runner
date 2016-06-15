package parser;


import Core.Job;
import Core.JobDefinition;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import util.Argument;

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

    public static ArrayList<JobDefinition> parseJobs(File jobFile) {
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

            ArrayList<JobDefinition> jobList = new ArrayList();

            // per Job
            for (Element jobElement : jobs) {
                String jobName = jobElement.getAttribute("name");

                Node runnerNode = jobElement.getElementsByTagName("runner").item(0);
                String runner = parseChildnodeTextContent(runnerNode, "name");
                ArrayList<Argument> runnerArguments = parseArguments(runnerNode);

                Node jarNode = jobElement.getElementsByTagName("jar").item(0);
                String jarFile = parseChildnodeTextContent(jarNode, "path");
                ArrayList<Argument> jarArguments = parseArguments(jarNode);

                jobList.add(new JobDefinition(jarFile, runnerArguments, jarArguments, runner, jobName));
            }
            return jobList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseChildnodeTextContent(Node node, String childNodeName) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equalsIgnoreCase(childNodeName)) {
                return item.getTextContent();
            }
        }
        return null;
    }

    private static ArrayList<Argument> parseArguments(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equalsIgnoreCase("arguments")) {
                return getArguments(item);
            }
        }
        return null;
    }

    // TODO: this can rewritten now, it is only used for jobOverwriting
    public static void parseRunnerInfo(Node runnerNode, Job job) {
        NodeList runnerChildNodes = runnerNode.getChildNodes();
        for (int i = 0; i < runnerChildNodes.getLength(); i++) {
            Node item = runnerChildNodes.item(i);
            switch (item.getNodeName()) {
                case "name":
                    job.setRunner(item.getTextContent());
                    break;
                case "arguments":
//                    job.setRunnerArguments(getArguments(item , "-"));
                    job.setOrOverwriteRunnerArguments(getArguments(item));
                    // TODO: don't always set the prefix, just add it in concatArguements
                    break;
            }

        }
    }

    public static void parseJarInfo(Node jarNode, Job job) {
        // get jar path
        NodeList jarChildNodes = jarNode.getChildNodes();
        for (int i = 0; i < jarChildNodes.getLength(); i++) {
            Node item = jarChildNodes.item(i);
            switch (item.getNodeName()) {
                case "path":
                    job.setJarFile(item.getTextContent());
                    break;
                case "arguments":
//                    job.setJarArguments(getArguments(item, ""));
                    job.setOrOverwriteJarArguments(getArguments(item));
                    break;
            }
        }
    }

    private static ArrayList<Argument> getArguments(Node node) {
        ArrayList<Argument> argumentList = new ArrayList<>();

        NodeList arguments = node.getChildNodes();
        for (int i = 0; i < arguments.getLength(); i++) {
            Node argument = arguments.item(i);
            if (argument.getNodeType() == Node.ELEMENT_NODE) {
                String parameterName = ""; // there might not be a name for the argument
                NamedNodeMap attributes = argument.getAttributes();
                if (attributes.getLength() != 0) {
                    parameterName = argument.getAttributes().item(0).getNodeValue();
                }
                argumentList.add(new Argument(parameterName, argument.getTextContent()));
            }
        }
        return argumentList;
    }
}
