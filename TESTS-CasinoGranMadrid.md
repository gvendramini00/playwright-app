# Casino Gran Madrid Pre-Production — Test List

Environment: Casino Gran Madrid pre-production site
Base URL: `http://localhost:8080/api/test/cgm/`
Database: `192.168.16.56`

---

## Registration Tests

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/cgm/testCase001` | Open pre-production site and verify it loads |
| `GET /api/test/cgm/testCase002` | Click register button — validate modal appears |
| `GET /api/test/cgm/testCase003` | Fast registration via Veridas (camera flow) |
| `GET /api/test/cgm/testCase004` | Manual registration with a generated unique player |
| `GET /api/test/cgm/testCase005` | Manual registration with missing name — verify validation error |
| `GET /api/test/cgm/testCase006` | Manual registration with duplicate DNI/NIE — verify duplicate error |
| `GET /api/test/cgm/runAll` | Run all 6 CGM tests in sequence — returns downloadable Excel report |

---

## Response Format

| Prefix | HTTP Status | Meaning |
|--------|-------------|---------|
| `OK —` | `200` | Test passed |
| `KO —` | `500` | Test failed — includes error detail and screenshot path |
| `SKIPPED —` | `200` | Test intentionally skipped |

Screenshots are saved to `screenshots/cgm/`.

> `testCase003` (Veridas) requires camera permission. The browser is launched with `--use-fake-device-for-media-stream` so it works headless.
> Successful registration tests append the created player to `generated-users.csv`.
