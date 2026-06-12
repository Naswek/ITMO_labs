FROM openjdk:26-rc-slim

WORKDIR /app

COPY target/scala-3.3.0/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]