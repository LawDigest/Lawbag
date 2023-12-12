FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY . /app

RUN chmod +x ./gradlew

RUN ./gradlew clean build

ARG JAR_FILE=build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]