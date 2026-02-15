FROM eclipse-temurin:25-jdk-ubi10-minimal
WORKDIR /app
COPY target/review.jar review.jar
COPY src/main/resources/db/migration /app/db/migration
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "review.jar"]