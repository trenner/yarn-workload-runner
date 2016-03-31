package parser;

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

            NodeList experimentNodeList = doc.getElementsByTagName("experiment");

            ArrayList<Element> experiments = new ArrayList<>();

            for (int i = 0; i < experimentNodeList.getLength(); i++) {
                Node node = experimentNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    experiments.add((Element) node);
                }
            }

            ArrayList<Schedule> schedules = new ArrayList<>();

            for (Element experimentElement: experiments) {
                String experimentName = experimentElement.getAttribute("name");
                Schedule experimentSchedule = new Schedule(experimentName);

                NodeList jobTimes = experimentElement.getChildNodes();
                for (int i = 0; i < jobTimes.getLength(); i++){
                    Node jobTime = jobTimes.item(i);
                    if (jobTime.getNodeType() == Node.ELEMENT_NODE) {
                        String jobName = jobTime.getAttributes().getNamedItem("name").getNodeValue();
                        Long delay = Long.parseLong(jobTime.getTextContent());
                        experimentSchedule.add(jobFactory.getJob(experimentSchedule.getExperimentName(), jobName, delay));
                    }
                }
                schedules.add(experimentSchedule);
            }
            // per experiment
            return schedules;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
