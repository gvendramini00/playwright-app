# Alira Staging — Test List

Environment: `https://gms-staging.pre.tecnalis.com/alira-server/`
Base URL: `http://localhost:8080/api/test/alira-staging/`
Database: `10.64.134.11`

---

## Mass Test Runner

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/alira-staging/runAll` | Run all Alira Staging tests in sequence and download an Excel report |

---

## Core Tests

| Endpoint | ID | Description |
|----------|----|-------------|
| `GET /api/test/alira-staging/testCase001` | NL2-TC-001 | Login to Alira Staging Back Office |
| `GET /api/test/alira-staging/testCase002` | NL2-TC-002 | Navigate to Staging Player Profile |

---

## CMS Tests

| Endpoint | ID | Description |
|----------|----|-------------|
| `GET /api/test/alira-staging/testCase003` | NL2-TC-003 | Website → CMS → Banners → edit and restore Banner Landscape 1 |

---

## Response Format

| Prefix | HTTP Status | Meaning |
|--------|-------------|---------|
| `OK —` | `200` | Test passed |
| `KO —` | `500` | Test failed — includes error detail and screenshot path |
| `SKIPPED —` | `200` | Test intentionally skipped |

Screenshots are saved to `screenshots/staging/`.

> Staging DB credentials (`STAGING_DB_USERNAME`, `STAGING_DB_PASSWORD`) are loaded lazily — the app starts without them and only requires them when a staging endpoint is first called.
