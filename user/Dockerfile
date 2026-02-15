FROM eclipse-temurin:25-jdk-ubi10-minimal
WORKDIR /app
COPY target/user.jar user.jar
COPY src/main/resources/db.migration /app/db/migration
EXPOSE 8084
ENTRYPOINT ["java", "-jar","user.jar"]