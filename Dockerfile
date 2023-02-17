FROM openjdk:13-jdk-alpine
RUN apk update && apk add bash
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY wait-for-it.sh wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]