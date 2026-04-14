# MyPlayRightApp — Setup Guide for Eclipse

A Spring Boot REST API that exposes Playwright browser automation tests as HTTP endpoints.
Trigger a test case by calling its URL; the app launches a browser, executes the flow, saves a screenshot, and returns a result string.

Supports three brands: **Alira** (back office), **Casino Gran Madrid (CGM)**, and **Golden Park Portugal (GP-PT)**.

---

## Requirements

| Tool    | Version |
|---------|---------|
| Java    | 17+     |
| Maven   | 3.6+    |
| MySQL   | Accessible dev/pre-prod instances for CGM and GP-PT |
| Eclipse | 2022-06 or newer (with Spring Tools 4 plugin recommended) |

---

## 1. Install Spring Tools 4 (Recommended)

Spring Tools 4 (STS4) adds Spring Boot run configuration support directly in Eclipse.

1. Go to **Help → Eclipse Marketplace**
2. Search for **Spring Tools 4**
3. Click **Install** and follow the prompts
4. Restart Eclipse when prompted

> Alternatively, download the pre-bundled [Spring Tools 4 for Eclipse](https://spring.io/tools) distro which includes everything out of the box.

---

## 2. Clone and Import the Project

### Option A — Clone from Eclipse

1. Go to **File → Import → Git → Projects from Git**
2. Select **Clone URI** and paste the repository URL
3. Follow the wizard, selecting **Import as general project** on the last step
4. After cloning, right-click the project → **Configure → Convert to Maven Project**

### Option B — Clone from the terminal, then import

```bash
git clone https://github.com/<your-org>/playwright-app.git
```

Then in Eclipse:

1. Go to **File → Import → Maven → Existing Maven Projects**
2. Set the **Root Directory** to the cloned folder
3. Check `pom.xml` and click **Finish**

Eclipse will download all dependencies automatically.

---

## 3. Set the Java Version

1. Right-click the project → **Properties → Java Compiler**
2. Uncheck **Use compliance from execution environment**
3. Set **Compiler compliance level** to **17**
4. Click **Apply and Close**

Also verify the JRE:

1. Right-click the project → **Properties → Java Build Path → Libraries**
2. Confirm the JRE System Library is Java 17 or higher
3. If not, click **Edit** and select the correct JRE

---

## 4. Configure Environment Variables

Credentials are never stored in any committed file. Set them in the run configuration.

1. Right-click `MyPlayRightAppApplication.java` → **Run As → Run Configurations**
2. Select the existing **Spring Boot App** entry (or create one under **Spring Boot App**)
3. Go to the **Environment** tab
4. Click **New** for each variable below and fill in your own values:

| Name | Value |
|------|-------|
| `SPRING_PROFILES_ACTIVE` | `dev` |
| `ALIRA_USERNAME` | your Alira username |
| `ALIRA_PASSWORD` | your Alira password |
| `CGM_DB_USERNAME` | your CGM DB username |
| `CGM_DB_PASSWORD` | your CGM DB password |
| `GPPT_DB_USERNAME` | your GP-PT DB username |
| `GPPT_DB_PASSWORD` | your GP-PT DB password |

5. Click **Apply → Run**

Eclipse stores these values in your local workspace only — they are never committed to Git.

> See `.env.example` at the project root for the full list of variables and their descriptions.

> **No Spring Tools installed?** Use a plain **Java Application** run config instead:
> right-click `MyPlayRightAppApplication.java` → **Run As → Run Configurations → Java Application**.
> The Environment tab works the same way.

---

## 5. Install Playwright Browsers

Open a terminal in the project root (or use **Window → Show View → Terminal** in Eclipse) and run:

**PowerShell (Windows):**
```powershell
./mvnw exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install --with-deps chromium"
```

**Bash / Linux / macOS:**
```bash
./mvnw exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps chromium"
```

This downloads the Chromium binary used by all tests. Run once after cloning.

---

## 6. Build the Project

Right-click the project → **Run As → Maven build...**

In the **Goals** field enter:

```
clean package -DskipTests
```

Click **Run**.

Or from a terminal:

```bash
mvn clean package -DskipTests
```

---

## 7. Run the Application

Right-click `MyPlayRightAppApplication.java` → **Run As → Spring Boot App**

(If Spring Tools is not installed: **Run As → Java Application**)

The app starts on `http://localhost:8080`.

To confirm it is running, open your browser and go to:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Environment Profiles

| Profile       | Headless | SQL logging | When to use          |
|---------------|----------|-------------|----------------------|
| *(default)*   | `true`   | `true`      | CI / headless runs   |
| `dev`         | `false`  | `true`      | Local — watch browser|
| `prod`        | `true`   | `false`     | Production           |

The run configuration above sets `SPRING_PROFILES_ACTIVE=dev`, which opens a visible browser window when tests run. Remove that variable or change it to `prod` for fully headless execution.

---

## 8. Record a New Test

Use the Playwright code generator to record interactions on any page.

Open a terminal in the project root and run:

**PowerShell (Windows):**
```powershell
./mvnw exec:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=codegen https://your-site-url.com"
```

**Bash / Linux / macOS:**
```bash
./mvnw exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen https://your-site-url.com"
```

A browser and a code panel will open. Interact with the page and copy the generated Java selectors into your service implementation.

---

## Test Endpoints

Base URL: `http://localhost:8080/api/test/`

### Alira — Back Office

| Endpoint | Description |
|----------|-------------|
| `GET /api/test/alira/testCase001` | Login to Alira Back Office |
| `GET /api/test/alira/testCase002` | Navigate to Player Profile |
| `GET /api/test/alira/navigation/games/testCase003` | Navigate → Games → Rooms |
| `GET /api/test/alira/navigation/games/testCase004` | Navigate → Games → Process Rooms |
| `GET /api/test/alira/navigation/games/testCase005` | Navigate → Games → Lobby Rooms |
| `GET /api/test/alira/navigation/games/testCase006` | Navigate → Games → Providers |
| `GET /api/test/alira/navigation/games/testCase007` | Navigate → Games → Themes & Tags |
| `GET /api/test/alira/navigation/games/testCase008` | Navigate → Games → Exchange Profile |
| `GET /api/test/alira/navigation/website/testCase009` | Navigate → Website → CMS |
| `GET /api/test/alira/navigation/website/testCase010` | Navigate → Website → Configuration → CMS Access |
| `GET /api/test/alira/navigation/website/testCase011` | Navigate → Website → Configuration → Constants |
| `GET /api/test/alira/navigation/marketing/testCase012` | Navigate → Dashboard — verify table has data |
| `GET /api/test/alira/navigation/marketing/testCase013` | Navigate → Marketing → Bonus → Deposit Promotions — verify table has data |
| `GET /api/test/alira/navigation/marketing/testCase014` | Navigate → Marketing → Bonus → Deposit Promotions → click New — verify modal appears |
| `GET /api/test/alira/navigation/marketing/testCase015` | Navigate → Marketing → Bonus → Deposit Promotions → create a new deposit promotion |
| `GET /api/test/alira/navigation/marketing/testCase016` | Navigate → Marketing → Bonus → Deposit Promotions → edit the first row and verify the update |
| `GET /api/test/alira/navigation/marketing/testCase017` | Navigate → Marketing → Bonus → Deposit Promotions → delete the first row and verify removal |

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

## Screenshots

Every test saves a screenshot of its final state under `/screenshots/`.
The folder is git-ignored. Screenshots are named after the test case (e.g., `testCase001.png`) or timestamped for runs that create unique data.

---

## Generated Test Data

Successful registration tests log the created user to `generated-users.csv` (email, national ID, alias, screenshot path). This file serves as an audit trail for test-created accounts.

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
