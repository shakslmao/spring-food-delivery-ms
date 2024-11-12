FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/restaurant-server-0.0.1-SNAPSHOT.jar /app/restaurant-server.jar

EXPOSE 8050

ENTRYPOINT [ "java", "-jar", "/app/restaurant-server.jar" ]