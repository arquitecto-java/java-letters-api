FROM openjdk:13-jdk-alpine
RUN apk update && apk add bash
ENV DBNAME=$DATABASE_HOST
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY wait-for-it.sh wait-for-it.sh
ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["./wait-for-it.sh", "${DBNAME}:3306", "--", "java", "-jar", "app.jar"]