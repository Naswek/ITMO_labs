# 1. Берем JDK
FROM openjdk:26-rc-slim

WORKDIR /app





COPY target/scala-2.13/*.jar app.jar

# Экспонируем порт
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]