FROM eclipse-temurin:25-jdk-ubi10-minimal
WORKDIR /app
COPY target/gateway.jar gateway.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","gateway.jar"]