FROM openjdk:11-jre-slim
WORKDIR /app
COPY .env /app/.env
COPY target/config-server-0.0.1-SNAPSHOT.jar /app/config-server.jar
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
