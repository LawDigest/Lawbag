<div align="center">

# Lawbag

**모두의입법의 Spring Boot 기반 백엔드 서비스**

![Spring Boot 3.1](https://img.shields.io/badge/Spring_Boot_3.1-6DB33F?style=flat-square&logo=springboot&logoColor=white) ![Java 17](https://img.shields.io/badge/Java_17-ED8B00?style=flat-square&logo=openjdk&logoColor=white) ![MySQL 8](https://img.shields.io/badge/MySQL_8-4479A1?style=flat-square&logo=mysql&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-FF4438?style=flat-square&logo=redis&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)

[English](./README.en.md)

</div>

---

## 소개

Lawbag은 모두의입법의 초기 백엔드 저장소입니다. 법안, 의원, 정당, 사용자 인증, 캐시, OAuth2, JWT, Swagger 문서화, 모니터링을 Spring Boot 기반으로 다룹니다.

애플리케이션 코드는 `lawmaking/` 아래에 있으며, Gradle 빌드와 Docker/Compose, GitHub Actions 배포 workflow가 함께 포함되어 있습니다.

## 주요 기능

| 기능 | 설명 |
|---|---|
| REST API 서버 | Spring Boot Web 기반 API 서버입니다. |
| 인증/인가 | Spring Security, OAuth2 Client, JWT 의존성을 포함합니다. |
| 데이터 계층 | Spring Data JPA, QueryDSL, MySQL connector를 사용합니다. |
| 캐시와 운영 지표 | Redis와 Actuator, Prometheus/Grafana compose 구성을 포함합니다. |
| 컨테이너 배포 | GitHub Actions에서 Gradle build 후 Docker image를 빌드·배포합니다. |

## 저장소 구조

| 경로 | 역할 |
|---|---|
| lawmaking/ | Spring Boot application root |
| lawmaking/src/main/java/com/everyones/lawmaking/ | Java application source |
| lawmaking/src/main/resources/ | application profiles and logging config |
| lawmaking/docker-compose.yml | Local MySQL, Redis, Prometheus, Grafana stack |
| .github/workflows/ | Build/deploy and development workflows |

## 빠른 시작

### 애플리케이션 디렉터리 이동

```bash
cd lawmaking
```

### 로컬 인프라 실행

```bash
docker compose up -d mysql redis prometheus grafana
```

### 테스트

```bash
./gradlew test
```

### 개발 실행

```bash
./gradlew bootRun
```

### 빌드

```bash
./gradlew build
```

## 검증

| 항목 | 명령 |
|---|---|
| Gradle tests | `cd lawmaking && ./gradlew test` |
| Gradle build | `cd lawmaking && ./gradlew build` |

## 운영 메모

- 로컬 compose 파일에는 DB/Redis/Prometheus/Grafana가 포함되어 있습니다. 실제 운영 secret은 GitHub Actions secret과 실행 환경에서 관리합니다.
- 배포 workflow는 JDK 17, Gradle build, Docker buildx, SSH 배포 단계를 사용합니다.
- 루트 README는 백엔드 역할을 설명하고, 실제 실행은 `lawmaking/` 기준으로 진행합니다.

## 문서 작성 근거

이 README는 저장소 안의 다음 파일과 문서를 기준으로 작성했습니다.

- `README.md`
- `lawmaking/build.gradle`
- `lawmaking/docker-compose.yml`
- `.github/workflows/back-deploy.yml`
