#!/bin/sh

export JAR_NAME=`ls ~/java-letters-api/api/build/libs/`
sudo java -jar ~/java-letters-api/api/build/libs/$JAR_NAME --spring.profiles.active=test --spring.datasource.password=
