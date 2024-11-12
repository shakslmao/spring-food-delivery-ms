FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/customer-server-0.0.1-SNAPSHOT.jar /app/customer-server.jar

EXPOSE 8090

ENTRYPOINT [ "java", "-jar", "/app/customer-server.jar" ]