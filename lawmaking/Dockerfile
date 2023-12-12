FROM openjdk:17-jdk-slim

RUN chmod +x ./gradlew

RUN ./gradlew clean build

ARG JAR_FILE=./build/libs/lawmaking-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]