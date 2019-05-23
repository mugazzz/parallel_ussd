#!/bin/bash

java

    if [ $? -eq 1 ]; then
       echo "rerunning failed scenario"
       mkdir target/Automation_First_round
       cp -R target/cucumber-html-reports/ target/Automation_First_round/
    	mvn test -Dcucumber.options="@target/rerun.txt"
       echo $?
    fi

exit 0