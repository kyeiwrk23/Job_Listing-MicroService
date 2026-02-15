FROM eclipse-temurin:25-jdk-ubi10-minimal
WORKDIR /app
COPY target/company.jar company.jar
COPY src/main/resources/db/migration /app/db/migration
EXPOSE 8081
ENTRYPOINT ["java","-jar","company.jar"]