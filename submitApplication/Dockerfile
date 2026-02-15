FROM eclipse-temurin:25-jdk-ubi10-minimal
WORKDIR /app
COPY target/application.jar application.jar
COPY src/main/resources/db/migration /app/db/migration
EXPOSE 8083
ENTRYPOINT ["java", "-jar","application.jar"]