FROM openjdk:17-jdk-slim

WORKDIR /Lawbag/lawmaking

RUN chmod +x ./gradlew

RUN ./gradlew clean build

ARG JAR_FILE=/Lawbag/lawmaking/build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]