# MyPlayRightApp — Automated UI Testing with Playwright + Spring Boot

A Spring Boot REST API that exposes Playwright browser automation tests as HTTP endpoints.
Trigger a test case by calling its URL; the app launches a browser, executes the flow, saves a screenshot, and returns a result string.

---

## Setup Guides

| IDE | Guide |
|-----|-------|
| IntelliJ IDEA | [README-IntelliJ.md](README-IntelliJ.md) |
| Eclipse | [README-Eclipse.md](README-Eclipse.md) |

---

## Test Lists

| Environment | File |
|-------------|------|
| Alira Pre-Production | [TESTS-AliraPre.md](TESTS-AliraPre.md) |
| Alira Staging | [TESTS-AliraStaging.md](TESTS-AliraStaging.md) |
| Casino Gran Madrid Pre | [TESTS-CasinoGranMadrid.md](TESTS-CasinoGranMadrid.md) |
| Golden Park Portugal Pre | [TESTS-GoldenParkPT.md](TESTS-GoldenParkPT.md) |

---

## Swagger UI

Interactive endpoint explorer with one dedicated page per environment:

```
http://localhost:8080/swagger-ui/index.html
```

Use the dropdown in the top-right corner to switch between:

| Group | Covers |
|-------|--------|
| 1 — Alira Pre-Production | Core, Games, Website, Marketing, Widgets, Mass Runner |
| 2 — Alira Staging | Staging tests + Staging Mass Runner |
| 3 — Casino Gran Madrid Pre | CGM registration tests |
| 4 — Golden Park Portugal Pre | GP-PT registration tests |

---

## Environment Variables

Copy `.env.example` to `.env`, fill in the values, and load it via your IDE or shell. `.env` is git-ignored and must never be committed.

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `ALIRA_USERNAME` | Yes | — | Alira Pre back office username |
| `ALIRA_PASSWORD` | Yes | — | Alira Pre back office password |
| `ALIRA_LOGIN_URL` | No | `https://gms.pre.tecnalis.com/alira-server/login.jsp` | Alira Pre login URL |
| `ALIRA_BASE_URL` | No | `https://gms.pre.tecnalis.com/alira-server/` | Alira Pre base URL |
| `ALIRA_STAGING_USERNAME` | Yes | — | Alira Staging back office username |
| `ALIRA_STAGING_PASSWORD` | Yes | — | Alira Staging back office password |
| `ALIRA_STAGING_LOGIN_URL` | No | `https://gms-staging.pre.tecnalis.com/alira-server/login.jsp` | Alira Staging login URL |
| `ALIRA_STAGING_BASE_URL` | No | `https://gms-staging.pre.tecnalis.com/alira-server/` | Alira Staging base URL |
| `STAGING_DB_USERNAME` | Yes* | — | Alira Staging database username |
| `STAGING_DB_PASSWORD` | Yes* | — | Alira Staging database password |
| `STAGING_DB_URL` | No | `jdbc:mysql://10.64.134.11:3306/OGP?useSSL=false&serverTimezone=UTC` | Alira Staging DB URL |
| `CGM_DB_USERNAME` | Yes | — | Casino Gran Madrid database username |
| `CGM_DB_PASSWORD` | Yes | — | Casino Gran Madrid database password |
| `CGM_DB_URL` | No | `jdbc:mysql://192.168.16.56:3306/OGP?useSSL=false&serverTimezone=UTC` | CGM DB URL |
| `GPPT_DB_USERNAME` | Yes | — | Golden Park Portugal database username |
| `GPPT_DB_PASSWORD` | Yes | — | Golden Park Portugal database password |
| `GPPT_DB_URL` | No | `jdbc:mysql://192.168.16.21:3306/OGP?useSSL=false&serverTimezone=UTC` | GP-PT DB URL |

> \* Staging datasource is lazy — the app starts without staging credentials. They are only required when a staging endpoint is first called.

---

## Screenshots

Every test saves a screenshot under `screenshots/<brand>/`. The folder is git-ignored.
Failed tests append `_failed` to the filename (e.g. `staging_testCase003_failed_<timestamp>.png`).

| Environment | Folder |
|-------------|--------|
| Alira Pre | `screenshots/alira/` |
| Alira Staging | `screenshots/staging/` |
| Casino Gran Madrid | `screenshots/cgm/` |
| Golden Park PT | `screenshots/gp/` |

---

## CI/CD

Designed for Jenkins pipeline integration. After each merge to `main`, the pipeline triggers the mass runner endpoints and parses `OK —` / `KO —` prefixes to determine pass/fail. Reports can be distributed via Slack or email.

Required credentials in the Jenkins credentials store:

- `ALIRA_USERNAME`, `ALIRA_PASSWORD`
- `ALIRA_STAGING_USERNAME`, `ALIRA_STAGING_PASSWORD`
- `STAGING_DB_USERNAME`, `STAGING_DB_PASSWORD`
- `CGM_DB_USERNAME`, `CGM_DB_PASSWORD`
- `GPPT_DB_USERNAME`, `GPPT_DB_PASSWORD`

---

## Dependencies

| Library | Version |
|---------|---------|
| Java | 17+ |
| Spring Boot | 3.5 |
| Playwright for Java | 1.44 |
| Spring Data JPA + MySQL Connector | latest |
| SpringDoc OpenAPI (Swagger UI) | 2.x |
| Apache POI (Excel reports) | 5.x |
| Lombok | 1.18+ |

---

© 2026 GIG Quality Team
