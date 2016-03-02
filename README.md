# yarn timed workload generator


## Instructions

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
java -jar target/YarnTimer-1.0-SNAPSHOT.jar src/main/resources/jobs.xml src/main/resources/schedule.xml
```