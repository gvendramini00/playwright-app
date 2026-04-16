# Golden Park Portugal Pre-Production — Test List

Environment: Golden Park Portugal dev site
Base URL: `http://localhost:8080/api/test/gp-pt/`
Database: `192.168.16.21`

---

## Registration Tests

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/gp-pt/register` | Manual registration with a generated unique PT player |
| `GET /api/test/gp-pt/register-duplicate` | Registration with duplicate NIF — verify duplicate error |

---

## Response Format

| Prefix | HTTP Status | Meaning |
|--------|-------------|---------|
| `OK —` | `200` | Test passed |
| `KO —` | `500` | Test failed — includes error detail and screenshot path |
| `SKIPPED —` | `200` | Test intentionally skipped |

Screenshots are saved to `screenshots/gp/`.

> Successful registration tests append the created player to `generated-users.csv`.
