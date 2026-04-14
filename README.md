# MyPlayRightApp — Automated UI Testing with Playwright + Spring Boot

A Spring Boot REST API that exposes Playwright browser automation tests as HTTP endpoints.
Trigger a test case by calling its URL; the app launches a browser, executes the flow, saves a screenshot, and returns a result string.

Supports three brands: **Alira** (back office), **Casino Gran Madrid (CGM)**, and **Golden Park Portugal (GP-PT)**.

---

## Requirements

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.6+ |
| MySQL | Accessible dev/pre-prod instances for CGM and GP-PT |

---

## Getting Started

### 1. Clone the project

```bash
git clone https://github.com/<your-org>/playwright-app.git
cd playwright-app
```

### 2. Configure environment variables

Copy the example file and fill in the values:

```bash
cp .env.example .env
```

Edit `.env` with the real credentials:

```dotenv
ALIRA_USERNAME=your_alira_user
ALIRA_PASSWORD=your_alira_password

CGM_DB_USERNAME=your_cgm_db_user
CGM_DB_PASSWORD=your_cgm_db_password

GPPT_DB_USERNAME=your_gppt_db_user
GPPT_DB_PASSWORD=your_gppt_db_password
```

> **.env is git-ignored and must never be committed.**  
> See `.env.example` for all available variables and their defaults.

### 3. Build the project

```bash
mvn clean package -DskipTests
```

### 4. Install Playwright browsers

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps chromium"
```

This downloads the Chromium binary used by all tests. Run once after cloning.

### 5. Run the application

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`.

---

## Environment Profiles

| Profile | How to activate | Headless | SQL logging |
|---------|----------------|----------|-------------|
| *(default)* | — | `true` | `true` |
| `dev` | `--spring.profiles.active=dev` | `false` | `true` |
| `prod` | `--spring.profiles.active=prod` | `true` | `false` |

Use the `dev` profile locally when you want to watch the browser:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Test Endpoints

Base URL: `http://localhost:8080/api/test/`

### Alira — Back Office

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/alira/testCase001` | Login to Alira Back Office |
| `GET /api/test/alira/testCase002` | Navigate to Player Profile |
| `GET /api/test/alira/games/testCase003` | Navigate → Games → Rooms |
| `GET /api/test/alira/games/testCase004` | Navigate → Games → Process Rooms |
| `GET /api/test/alira/games/testCase005` | Navigate → Games → Lobby Rooms |
| `GET /api/test/alira/games/testCase006` | Navigate → Games → Providers |
| `GET /api/test/alira/games/testCase007` | Navigate → Games → Themes & Tags |
| `GET /api/test/alira/games/testCase008` | Navigate → Games → Exchange Profile |
| `GET /api/test/alira/website/testCase009` | Navigate → Website → CMS |
| `GET /api/test/alira/website/testCase010` | Navigate → Website → Configuration → CMS Access |
| `GET /api/test/alira/website/testCase011` | Navigate → Website → Configuration → Constants |

### Casino Gran Madrid (CGM)

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/cgm/testCase001` | Open pre-production site |
| `GET /api/test/cgm/testCase002` | Click register button, validate modal |
| `GET /api/test/cgm/testCase003` | Fast registration via Veridas (camera flow) |
| `GET /api/test/cgm/testCase004` | Manual registration with a generated unique player |
| `GET /api/test/cgm/testCase005` | Manual registration with missing name (validation check) |
| `GET /api/test/cgm/testCase006` | Manual registration with duplicate DNI/NIE (duplicate check) |

### Golden Park Portugal (GP-PT)

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/gp-pt/register` | Manual registration with a generated unique PT player |
| `GET /api/test/gp-pt/register-duplicate` | Registration with duplicate NIF (duplicate check) |

---

## API Documentation (Swagger UI)

Interactive endpoint explorer available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Screenshots

Every test saves a screenshot of its final state under `/screenshots/`.  
The folder is git-ignored. Screenshots are named after the test case (e.g., `testCase001.png`) or timestamped for runs that create unique data.

---

## Generated Test Data

Successful registration tests log the created user to `generated-users.csv` (email, national ID, alias, screenshot path). This file serves as an audit trail for test-created accounts.

---

## Recording New Tests

Use the Playwright code generator to record interactions on any page:

```bash
./mvnw exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen https://your-site-url.com"
```

---

## CI/CD

A GitHub Actions workflow (`.github/workflows/ci.yml`) runs on every push and pull request.  
It compiles the project and verifies the build. E2E tests are skipped in CI by default because they require live database access and a Playwright-capable runner.

To enable full E2E runs on a self-hosted runner, add the following secrets to your GitHub repository and uncomment the relevant steps in `ci.yml`:

- `ALIRA_USERNAME`, `ALIRA_PASSWORD`
- `CGM_DB_USERNAME`, `CGM_DB_PASSWORD`
- `GPPT_DB_USERNAME`, `GPPT_DB_PASSWORD`

---

## Notes

- Tests use a real database connection to avoid generating duplicate players. Ensure the DB is reachable before running registration tests.
- `testCase003` (Veridas) requires camera permission — the browser is launched with `--use-fake-device-for-media-stream` so it works headless.
- Timeouts and selectors can be adjusted in the corresponding service implementation class.

---

## Dependencies

- Java 17+
- Spring Boot 3.5
- Playwright for Java 1.44
- Spring Data JPA + MySQL
- SpringDoc OpenAPI (Swagger)

---

© 2025 GIG Quality Team
