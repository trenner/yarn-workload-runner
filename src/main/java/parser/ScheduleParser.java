package parser;

import Core.Job;
import Core.JobSequence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.JobFactory;
import util.Schedule;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by joh-mue on 03/02/16.
 */
public class ScheduleParser {

    public static ArrayList<Schedule> parseSchedule(File scheduleFile, JobFactory jobFactory) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(scheduleFile);
            doc.getDocumentElement().normalize();

            ArrayList<Element> experiments = new ArrayList<>();

            NodeList experimentNodeList = doc.getElementsByTagName("experiment");
            for (int i = 0; i < experimentNodeList.getLength(); i++) {
                Node node = experimentNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    experiments.add((Element) node);
                }
            }

            ArrayList<Schedule> schedules = new ArrayList<>();

            for (Element experimentElement: experiments) {
                String expName = experimentElement.getAttribute("name");
                Schedule experimentSchedule = new Schedule(expName);

                for (Node node: getChildIterable(experimentElement)) {
                    if (node.getNodeName().equalsIgnoreCase("job-sequence")) {
                        JobSequence jobSequence = new JobSequence();
                        getChildIterable(node).forEach(childNode -> parseJob(childNode, jobSequence, jobFactory, expName));
                        experimentSchedule.add(jobSequence);
                    } else { // element is a plain job -> jobSequence with one job in it
                        JobSequence jobSequence = new JobSequence();
                        parseJob(node, jobSequence, jobFactory, expName);
                        experimentSchedule.add(jobSequence);
                    }
                }

                schedules.add(experimentSchedule);
            }
            // per experiment
            return schedules;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void parseJob(Node jobNode, JobSequence jobSequence, JobFactory jobFactory, String expName) {
        String jobName = jobNode.getAttributes().getNamedItem("name").getNodeValue();

        Job job;
        if (jobNode.getTextContent().isEmpty()) { // no value defaults to 0
            Long delay = new Long(0);

            Node delayNode = ((Element) jobNode).getElementsByTagName("delay").item(0);
            if (delayNode != null) {
                delay = Long.parseLong(delayNode.getTextContent());
            }

            // TODO: this could be simplified and combined into the jobfactory?
            job = jobFactory.getJob(expName, jobName, delay);
            JobParser.overwriteJobInfo(jobNode, job);

        } else {
            Long delay = Long.parseLong(jobNode.getTextContent());
            job = jobFactory.getJob(expName, jobName, delay);
        }

        jobSequence.add(job);
    }

    /**
     * Returns an
     * @param parentNode
     * @return
     */
    private static Iterable<Node> getChildIterable(Node parentNode) {
        ArrayList<Node> childList = new ArrayList<>();

        NodeList nodeList = parentNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                childList.add(node);
            }
        }
        return childList;
    }
}
