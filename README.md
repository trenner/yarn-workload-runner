# yarn timed workload generator


## Instructions

make a jobs.xml file where all your jobs are defined

```
jobs.xml

<jobs>
    <job name="jobA">
        <framework>flink</framework>
        <jar>./examples/wordcount.jar</jar>
        <parameters>
            <parameter name="m">yarn-cluster</parameter>
            <parameter name="yn">4</parameter>
            <parameter name="yjm">1024</parameter>
            <parameter name="ytm">4096</parameter>
        </parameters>
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

in YarnTimedGenerator.java fill in the Locations of above files and then

```
mvn clean install
java -jar YarnTimer-1.0-SNAPSHOT.jar
```