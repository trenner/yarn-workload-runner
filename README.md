# yarn workload runner

The runner allows to execute multiple YARN applications: each with its own resource specification as well as arguments, all following a defined schedule.


## Building

Run
```mvn clean install```


## Running

The workload generator takes three arguments, one for each of three necessary xml config files: jobs,
experiment schedules, and system configurations. To start the workload generator execute

```YarnTimer.jar JOB_FILE SCHEDULE_FILE CONFIG_FILE```

e.g.
```
java -jar target/YarnWorkloadRunner-1.0-SNAPSHOT-jar-with-dependencies.jar src/main/resources/jobs.xml src/main/resources/schedule.xml src/main/resources/config.xml
```

Example configurations can be found as ```jobs.xml```, ```schedule.xml```, and ```config.xml``` in the ```main/resources``` folder as well as below.


## Configuration

create a config.xml file and set the required fields based on this example

```
config.xml

<config>
    <!-- general configuration -->

    <!-- set the HADOOP_HOME environment variable -->
    <hadoop-home>/path/to/hadoop-2.7.1/etc/hadoop/</hadoop-home>

    <!-- state where your flink instance resides -->
    <flink-home-dir>/path/to/flink-0.10.1/</flink-home-dir>

    <!-- state where your spark instance resides -->
    <spark-home-dir>/path/to/spark-1.6.2/</spark-home-dir>

    <!-- this is where logs will be stored -->
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

    <job name="sparkpi">
        <runner>
            <name>spark</name>
            <arguments> <!-- the arguments 'master' and 'deploy-mode' will be added per default-->
                <argument name="class">org.apache.spark.examples.SparkPi</argument>
                <!--<argument name="driver-memory">4g</argument>-->
                <!--<argument name="executor-memory">1g</argument>-->
                <!--<argument name="executor-cores">1</argument>-->
                <!--<argument name="queue">thequeue</argument>-->
            </arguments>
        </runner>
        <jar>
            <path>./lib/spark-examples-1.6.1-hadoop2.6.0.jar</path>
            <arguments>
                <argument>10</argument>
            </arguments>
        </jar>
    </job>
    ...
</jobs>
```

Note that for spark jobs the command is build with ```/bin/spark-submit --master yarn --deploy-mode cluster``` so the
arguments ```--master``` and ```--deploy-mode``` are not necessary for a spark job submission.


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

---

## Schedule and JobSequences

In addition to jobs that are submitted after a certain delay, you can also define job sequences in your schedule.xml. Job sequences will be
executed one after the other, not parallely. Thus in a sequence of 'jobA' followed by 'JobB' followed by 'JobC', JobC will not start
until after 'JobB' is finished and 'JobB' won't start until 'JobA' is. Jobs and job sequences may be mixed
in a schedule.xml

```
schedule.xml

<suite>
    <experiment name="exp1">
        <job name="jobA">1000</job>
        <job-sequence>
            <job name="jobA">2000</job>
            <job name="jobB">3000</job>
            <job name="jobc">3000</job>
        <job-sequence>
    </experiment>
    ...
</suite>
```

---

## Argument overwriting

In order to avoid cluttering the jobs.xml with almost identical copies of job definition that differ only in a few values, it is possible to overwrite values of any defined job in your schedule.xml.
Use the same xml hierarchy to overwrite values. Arguments that are not defined yet or have no name will be added to the
job definition.

Note: If you overwrite values, the delay cannot simply be added as text context but instead must be contained in its own
<delay> xml tag.

```
<suite>
    <experiment name="exp1">
        <job name="jobA">
            <delay>1000</delay>
            <runner>
                <arguments>
                    <argument name="key">value</argument>
                    <argument>value</argument>
                </arguments>
            </runner>
        </job>
    </experiment>
    ...
</suite>
```
