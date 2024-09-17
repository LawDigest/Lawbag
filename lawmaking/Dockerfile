FROM openjdk:17.0.2-jdk-alpine

ARG JAR_FILE=build/libs/lawmaking-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
