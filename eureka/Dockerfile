FROM eclipse-temurin:25-jdk-ubi10-minimal
WORKDIR /app
COPY target/eureka.jar eureka.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","eureka.jar"]