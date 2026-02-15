FROM eclipse-temurin:25-jdk-ubi10-minimal
WORKDIR /app
COPY target/job.jar job.jar
COPY src/main/resources/db/migration /app/db/migration
EXPOSE 8085
ENTRYPOINT ["java", "-jar","job.jar"]