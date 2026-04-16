# Alira Pre-Production — Test List

Environment: `https://gms.pre.tecnalis.com/alira-server/`
Base URL: `http://localhost:8080/api/test/alira/`

---

## Mass Test Runner

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/alira/mass/run-all` | Run all Alira Pre tests in sequence and download an Excel report |

---

## Core Tests

| Endpoint | ID | Description |
|----------|----|-------------|
| `GET /api/test/alira/testCase001` | TC-001 | Login to Alira Back Office |
| `GET /api/test/alira/testCase002` | TC-002 | Navigate to Player Profile |

---

## Games Tab

| Endpoint | ID | Description |
|----------|----|-------------|
| `GET /api/test/alira/navigation/games/testCase003` | TC-003 | Games → Rooms |
| `GET /api/test/alira/navigation/games/testCase004` | TC-004 | Games → Process Rooms |
| `GET /api/test/alira/navigation/games/testCase005` | TC-005 | Games → Lobby Rooms |
| `GET /api/test/alira/navigation/games/testCase006` | TC-006 | Games → Providers |
| `GET /api/test/alira/navigation/games/testCase007` | TC-007 | Games → Themes & Tags |
| `GET /api/test/alira/navigation/games/testCase008` | TC-008 | Games → Exchange Profile |

---

## Website Tab

| Endpoint | ID | Description |
|----------|----|-------------|
| `GET /api/test/alira/navigation/website/testCase009` | TC-009 | Website → CMS |
| `GET /api/test/alira/navigation/website/testCase010` | TC-010 | Website → Configuration → CMS Access |
| `GET /api/test/alira/navigation/website/testCase011` | TC-011 | Website → Configuration → Constants |

---

## Marketing Tab

| Endpoint | ID | Description |
|----------|----|-------------|
| `GET /api/test/alira/navigation/marketing/testCase012` | TC-012 | Dashboard — verify table has data |
| `GET /api/test/alira/navigation/marketing/testCase013` | TC-013 | Marketing → Bonus → Deposit Promotions — verify table |
| `GET /api/test/alira/navigation/marketing/testCase014` | TC-014 | Marketing → Bonus → Deposit Promotions → New modal |
| `GET /api/test/alira/navigation/marketing/testCase015` | TC-015 | Create a new Deposit Promotion |
| `GET /api/test/alira/navigation/marketing/testCase016` | TC-016 | Edit the first Deposit Promotion |
| `GET /api/test/alira/navigation/marketing/testCase017` | TC-017 | Delete the first Deposit Promotion |

---

## Player Profile Widgets

| Endpoint | ID | Description |
|----------|----|-------------|
| `GET /api/test/alira/widgets/testCase018` | TC-018 | Player Profile → Widgets → Attached Documentation → Add |

---

## Response Format

| Prefix | HTTP Status | Meaning |
|--------|-------------|---------|
| `OK —` | `200` | Test passed |
| `KO —` | `500` | Test failed — includes error detail and screenshot path |
| `SKIPPED —` | `200` | Test intentionally skipped |

Screenshots are saved to `screenshots/alira/`.
