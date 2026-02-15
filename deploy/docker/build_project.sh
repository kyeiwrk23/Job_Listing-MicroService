#!/bin/bash
cd ../..
# Build all services
#cd eureka && ./mvnw clean package -DskipTests && cd ..
#cd configserver && ./mvnw clean package -DskipTests && cd ..
#cd gateway && ./mvnw clean package -DskipTests && cd ..
#cd company && ./mvnw clean package -DskipTests && cd ..
cd jobs && ./mvnw clean package -DskipTests && cd ..
#cd review && ./mvnw clean package -DskipTests && cd ..
#cd submitApplication && ./mvnw clean package -DskipTests && cd ..
#cd user && ./mvnw clean package -DskipTests && cd ..
