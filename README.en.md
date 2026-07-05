<div align="center">

# Lawbag

**The Spring Boot backend service for LawDigest**

![Spring Boot 3.1](https://img.shields.io/badge/Spring_Boot_3.1-6DB33F?style=flat-square&logo=springboot&logoColor=white) ![Java 17](https://img.shields.io/badge/Java_17-ED8B00?style=flat-square&logo=openjdk&logoColor=white) ![MySQL 8](https://img.shields.io/badge/MySQL_8-4479A1?style=flat-square&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-FF4438?style=flat-square&logo=redis&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)

[한국어](./README.md)

</div>

---

## Overview

Lawbag is the earlier Spring Boot backend repository for LawDigest. It covers bill, lawmaker, party, authentication, caching, OAuth2, JWT, Swagger, and monitoring concerns.

The application lives under `lawmaking/` and includes Gradle build files, Docker/Compose assets, and a GitHub Actions deployment workflow.

## Highlights

| Area | Description |
|---|---|
| REST API server | A Spring Boot Web backend service. |
| Authentication and authorization | Includes Spring Security, OAuth2 Client, and JWT dependencies. |
| Data layer | Uses Spring Data JPA, QueryDSL, and the MySQL connector. |
| Cache and observability | Includes Redis, Actuator, Prometheus, and Grafana compose assets. |
| Container deployment | GitHub Actions builds with Gradle and deploys a Docker image. |

## Repository Structure

| Path | Role |
|---|---|
| lawmaking/ | Spring Boot application root |
| lawmaking/src/main/java/com/everyones/lawmaking/ | Java application source |
| lawmaking/src/main/resources/ | application profiles and logging config |
| lawmaking/docker-compose.yml | Local MySQL, Redis, Prometheus, Grafana stack |
| .github/workflows/ | Build/deploy and development workflows |

## Quick Start

### Enter app directory

```bash
cd lawmaking
```

### Start local infrastructure

```bash
docker compose up -d mysql redis prometheus grafana
```

### Run tests

```bash
./gradlew test
```

### Run app

```bash
./gradlew bootRun
```

### Build

```bash
./gradlew build
```

## Verification

| Check | Command |
|---|---|
| Gradle tests | `cd lawmaking && ./gradlew test` |
| Gradle build | `cd lawmaking && ./gradlew build` |

## Operational Notes

- The local compose file includes DB, Redis, Prometheus, and Grafana. Production secrets belong in GitHub Actions secrets and runtime configuration.
- The deployment workflow uses JDK 17, Gradle build, Docker buildx, and SSH deployment steps.
- The root README documents the backend role; commands should be run from `lawmaking/`.

## Documentation Sources

This README was written from the following files and documents in this repository.

- `README.md`
- `lawmaking/build.gradle`
- `lawmaking/docker-compose.yml`
- `.github/workflows/back-deploy.yml`
