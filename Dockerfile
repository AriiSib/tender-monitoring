FROM eclipse-temurin:21-jdk

WORKDIR /app

RUN mkdir -p /app/logs

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]