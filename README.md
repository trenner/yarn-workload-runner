# yarn timed workload generator

The workload generator takes three arguments related to the three necessary xml files than contain the different jobs,
experiment schedules and system configurations. To start the workload generator execute

```YarnTimer.jar JOB_FILE SCHEDULE_FILE CONFIG_FILE```

Example configurations can be found as ```jobs.xml```, ```schedule.xml```, and ```config.xml``` in the ```main/resources``` folder.

The YarnTimer.jar takes three arguments in the following order:


## Instructions

create a config.xml file and set the required fields based on this example

```
config.xml

<config>
    <!-- general configuration -->

    <!-- to set the HADOOP_HOME environment variable -->
    <hadoop-home>/path/to/hadoop-2.7.1/etc/hadoop/</hadoop-home>

    <!-- state where your flink instance resides -->
    <flink-home-dir>/path/to/flink-0.10.1/</flink-home-dir>

    <!-- state where your spark instance resides -->
    <spark-home-dir>/path/to/flink-0.10.1/</flink-home-dir>

    <!-- this is where logs will be saved to -->
    <log-dir>/path/to/logs/</log-dir>

    <!-- do you want to be prompted when logs already exist? -->
    <overwriteLogs>false</overwriteLogs>

    <!-- Freamon related configurations -->
    <notifyFreamon>false</notifyFreamon>
        <akkaHost>cluster01.example.com</akkaHost>
        <akkaPort>1234</akkaPort>
        <freamonMasterHost>cluster01.example.com</freamonMasterHost>
        <freamonMasterPort>1235</freamonMasterPort>
        <freamonMasterSystemName>masterSystem</freamonMasterSystemName>
        <freamonMasterActorName>monitorMaster</freamonMasterActorName>
</config>
```

make a jobs.xml file where all your jobs are defined


```
jobs.xml

<jobs>
    <job name="KMeans">
        <runner>
            <name>flink</name>
            <arguments>
                <argument>value</argument>
                <argument name="key">value</argument>
                <argument name="m">yarn-cluster</argument>
                <argument name="yn">2</argument>
                <argument name="yjm">512</argument>
                <argument name="ytm">2048</argument>
            </arguments>
        </runner>
        <jar>
            <path>/examples/wordcount.jar</path>
            <arguments>
                <argument name="arg1">1</argument>
                <argument name="arg2">2</argument>
                <argument name="arg3">3</argument>
            </arguments>
        </jar>
    </job>
    ...
</jobs>
```

make a schedule.xml file where your experiment schedule is layed out

```
schedule.xml

<suite>
    <experiment name="exp1">
        <job name="jobA">1000</job>
        <job name="jobB">2000</job>
        <job name="jobA">3000</job>
    </experiment>
    ...
</suite>
```

the locations of the above files must be stated as arguments in the correct order

```
mvn clean install
java -jar target/YarnWorkloadRunner-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/jobs.xml src/main/resources/schedule.xml src/main/resources/config.xml
```
