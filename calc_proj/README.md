# Calculator API – Automated CI/CD Pipeline

A Spring Boot (Java 21) REST calculator API with a full CI/CD pipeline:
**GitHub → Jenkins → Maven → Docker → Docker Compose → Nginx**.

Every push to GitHub triggers Jenkins, which builds and tests the code, packages it as a Docker image, redeploys it behind an Nginx reverse proxy, and emails the result.

## Architecture

```
Developer ──git push──> GitHub ──webhook──> Jenkins
                                              │ Checkout → Build & Test → Docker Build → Deploy
                                              ▼
                              Docker Compose (calc-net network)
                              Nginx (host :8081 → :80) ──> Spring Boot app (:8888)
```

Only Nginx is published to the host. The Spring Boot container is reachable only through Nginx.

## Tech Stack

- Java 21, Spring Boot 4.1.2 (Web + Actuator), Maven (wrapper included)
- JUnit 5, MockMvc, AssertJ
- Docker (multi-stage build, non-root runtime), Docker Compose
- Nginx (alpine) as reverse proxy
- Jenkins declarative pipeline

## API Endpoints

All endpoints are `GET` and take query parameters `a` and `b` (doubles).

| Endpoint | Defaults | Example |
|---|---|---|
| `/api/v1/add` | a=0, b=0 | `/api/v1/add?a=7&b=3` → result `10.0` |
| `/api/v1/subtract` | a=0, b=0 | `/api/v1/subtract?a=10&b=5` |
| `/api/v1/multiply` | a=0, b=0 | `/api/v1/multiply?a=4&b=3` |
| `/api/v1/divide` | a=0, b=1 | `/api/v1/divide?a=10&b=2` |
| `/actuator/health` | – | Health check used by Docker Compose |

Success response:

```json
{ "info": { "operation": "add", "a": "7.0", "b": "3.0", "result": "10.0" } }
```

Dividing by zero returns **HTTP 400** with the text `Arithmetic Error - Division by zero is not allowed.`

## Running Locally

**Without Docker** (needs JDK 21):

```bash
./mvnw spring-boot:run
# app starts on port 8080 by default
```

**With Docker Compose** (app + Nginx):

```bash
docker compose up -d --build
curl "http://localhost:8081/api/v1/add?a=7&b=3"
curl http://localhost:8081/actuator/health
docker compose down
```

## Ports

| Component | Port | Set in |
|---|---|---|
| Spring Boot (container) | 8888 | `SERVER_PORT` in `docker-compose.yaml` |
| Healthcheck | 8888 | `docker-compose.yaml` |
| Nginx upstream | 8888 | `nginx.conf` |
| Nginx (container → host) | 80 → 8081 | `docker-compose.yaml` |
| Jenkins | 8080 | Jenkins default |

Keep these consistent. A wrong healthcheck port stops Nginx from starting, and a wrong upstream port gives `502 Bad Gateway`.

## Tests

```bash
./mvnw clean test
```

8 tests: `RESTControllerTest` (6, MockMvc), `ResponseMessageTest` (1), `CalcProjApplicationIntegrationTest` (1, full app over HTTP on a random port).

## CI/CD Pipeline (Jenkinsfile)

| Stage | What it does |
|---|---|
| Checkout | `checkout scm` |
| Build & Test | `./mvnw clean test package` in `calc_proj/`; publishes JUnit reports |
| Docker Build | `docker build -t calculator-api:latest .` |
| Deploy | `docker compose down` then `docker compose up -d` |
| Post | `docker image prune -f`; success/failure email |

### Jenkins setup

1. Install the **Git**, **GitHub**, **Pipeline** and **JUnit** plugins.
2. Register a JDK named **`Java 21`** under *Manage Jenkins → Tools*.
3. Make sure the Jenkins user can run Docker (`docker` and `docker compose`).
4. Configure SMTP under *Manage Jenkins → System* so the `mail` step works, and update the recipient address in the `Jenkinsfile`.
5. Create a Pipeline job: **Pipeline script from SCM**, Git, with this repository's URL.
6. Enable **GitHub hook trigger for GITScm polling**.
7. In GitHub → *Settings → Webhooks*, add `https://<public-jenkins-url>/github-webhook/` with the `push` event.
   If Jenkins runs locally, expose **Jenkins' port** (not the app's port 8081) through a public tunnel.

## Project Layout

```
.
├── Jenkinsfile
└── calc_proj/
    ├── Dockerfile
    ├── docker-compose.yaml
    ├── nginx.conf
    ├── pom.xml
    ├── mvnw
    └── src/                 # package com.spe.calc_proj
```

## Notes

- Docker Compose names resources after the directory (`calc_proj_calc-net`, `calc_proj-calculator-1`). The image is tagged explicitly as `calculator-api:latest` in both the Jenkinsfile and Compose file so they always refer to the same image.
