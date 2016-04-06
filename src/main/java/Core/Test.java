package Core;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by joh-mue on 11/03/16.
 */
public class Test {
    public static void main(String[] args) {
        parseDataPlacementFile("FileC");
    }

    private static ArrayList<String> parseDataPlacementFile(String src) {
        ArrayList<String> preferenceList = new ArrayList<>();

        // first check the Dflag hadoop.home.dir with JVM scope
        String home = System.getProperty("hadoop.home.dir");

        // fall back to the system/user-global env variable
        if (home == null) {
            home = System.getenv("HADOOP_HOME");
        }

        try{
//      FileInputStream fstream = new FileInputStream(new File(home, "etc/hadoop/file-node.map"));
            FileInputStream fstream = new FileInputStream(new File("/Users/Johannes/arbeit/ioaware/file-node.map"));
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = br.readLine()) != null)   {
                StringTokenizer stringTokenizer = new StringTokenizer(line);

                String id = stringTokenizer.nextToken();
                String path = stringTokenizer.nextToken();
                String dataNode = stringTokenizer.nextToken();

                StringTokenizer pathTokenizer = new StringTokenizer(path,",");
                while (pathTokenizer.hasMoreTokens()) {
                    if (pathTokenizer.nextToken().compareToIgnoreCase(src) == 0) {
                        StringTokenizer nodeTokenizer = new StringTokenizer(dataNode, ",");
                        while (nodeTokenizer.hasMoreTokens()) {
                            preferenceList.add(nodeTokenizer.nextToken());
                        }
                    }
                    if (!preferenceList.isEmpty()) {
                        return preferenceList;
                    }
                }
            }
            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        return null;
    }
}
