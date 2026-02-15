# **Job Listing Platform – Microservices Architecture (Spring Boot + Keycloak + Docker + Observability)**

A fully containerized, cloud‑ready Job Listing Platform built with Spring Boot microservices, secured with Keycloak OAuth2, orchestrated via Docker Compose, and monitored with a complete observability stack (Prometheus, Grafana, Zipkin).
The system follows a service‑oriented architecture with centralized configuration, service discovery, API gateway routing, and asynchronous communication.

## Architecture Overview
This project consists of the following microservices:

### Core Infrastructure

Eureka Server:- Service discovery for all microservices

Config Server:- Centralized configuration using Git‑backed config

API Gateway:- Single entry point, OAuth2 Resource Server (Keycloak)

### Business Domain Services
Company Service:- Manages companies/organizations that publish jobs

Job Service:- Manages job listings created by companies

Submit Application Service: Handles job applications submitted by users

User Service:- Manages user profiles and authentication integration

Review Service:- Users publish reviews for companies

### Observability Stack
Zipkin:- Distributed tracing

Prometheus:- Metrics scraping

Grafana:- Metrics visualization dashboards

### Database
* PostgreSQL for all microservices
* Flyway for schema versioning and migrations

### Deployment
* Fully containerized using Docker Compose
* One‑command startup

## High‑Level Architecture Diagram

                           ┌──────────────────────────┐
                           │      Config Server       │
                           │   (Centralized Config)   │
                           └─────────────┬────────────┘
                                         │
                           ┌─────────────▼────────────┐
                           │       Eureka Server      │
                           │   (Service Discovery)    │
                           └─────────────┬────────────┘
                                         │
                           ┌─────────────▼────────────┐
                           │       API Gateway        │
                           │  OAuth2 (Keycloak RS)    │
                           └─────────────┬────────────┘
                                         │
     ┌───────────────────────────────────┼────────────────────────────────────┐
     │                                   │                                    │
┌────▼─────┐                      ┌──────▼──────┐                      ┌──────▼──────┐
│ Company  │                      │   Job       │                      │  Review     │
│ Service  │◄───reviews────────── │  Service    │                      │  Service    │
└────┬─────┘                      └──────┬──────┘                      └──────┬──────┘
     │                                   │                                    │
     │                                   │                                    │
┌────▼──────┐                     ┌──────▼────────┐                    ┌──────▼────────┐
│ Submit    │                     │ User Service  │                    │ Keycloak Auth │
│Application│                     │               │                    │  (OAuth2)     │
└───────────┘                     └───────────────┘                    └───────────────┘

                     ┌──────────────────────────────────────────┐
                     │              Observability               │
                     │  Prometheus | Grafana | Zipkin           │
                     └──────────────────────────────────────────┘

                     ┌──────────────────────────────────────────┐
                     │              PostgreSQL DB               │
                     │        (Flyway schema migrations)        │
                     └──────────────────────────────────────────┘



## Tech Stack

### Backend

* Java 25
* Spring Boot 3
* Spring Cloud (Eureka, Config, Gateway, Stream)
* Spring Security + OAuth2 Resource Server
* Keycloak Identity Provider
* PostgreSQL + Flyway
* RabbitMQ 

### Observability

* Zipkin (Tracing)
* Prometheus (Metrics)
* Grafana (Dashboards)

### Deployment

* Docker
* Docker Compose

## Project Structure

/eureka-server

/config-server

/gateway

/company-service

/job-service

/submit-application-service

/user-service

/review-service

/observability

    /prometheus

    /grafana

    /zipkin

/deploy

    build_project.sh

    docker-compose.yml


## How to Run the Project

### 1. Build all microservice JARs

From the root directory:
cd deploy/docker
./build_project.sh

This script builds all services using Maven and copies the JARs into the deployment directory(target).

### 2️ Start the entire system with Docker
docker compose up --build -d

